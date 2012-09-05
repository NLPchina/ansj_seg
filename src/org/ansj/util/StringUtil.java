package org.ansj.util;

public class StringUtil {

    private static int[] filter = new int[128];
    private static int[] filterEnd = new int[128];

    static {
        filter['<'] = Integer.MAX_VALUE / 2;
        filterEnd['<'] = '>';

        filter['&'] = 10;
        filterEnd['&'] = ';';

        filter[';'] = -1;
        filter['\n'] = -1;

        filter['\r'] = -1;
        filter['\t'] = -1;
        filter[' '] = 1;
        filter['*'] = 1;
        filter['+'] = 1;
        filter['-'] = 1;
        filter['.'] = 1;
        filter['#'] = 1;

    }

    /**
     * 去除html标签
     *
     * @param input
     * @return
     */
    public static String rmHtmlTag(String input) {
        if (isBlank(input)) {
            return "";
        }
        int length = input.length();
        int tl = 0;
        StringBuilder sb = new StringBuilder();
        char c = 0;
        for (int i = 0; i < length; i++) {
            c = input.charAt(i);

            if (c > 127) {
                sb.append(c);
                continue;
            }

            switch (filter[c]) {
                case -1:
                    break;
                case 0:
                    sb.append(c);
                    break;
                case 1:
                    if (sb.length() > 0 && sb.charAt(sb.length() - 1) != c)
                        sb.append(c);
                    do {
                        i++;
                    } while (i < length && input.charAt(i) == c);

                    if (i < length || input.charAt(length - 1) != c)
                        i--;
                    break;
                default:
                    tl = filter[c] + i;
                    int tempOff = i;
                    boolean flag = false;
                    char end = (char) filterEnd[c];
                    for (i++; i < length && i < tl; i++) {
                        c = input.charAt(i);
                        if (c > 127) continue;
                        if (c == end) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        i = tempOff;
                        sb.append(input.charAt(i));
                    }
                    break;
            }
        }
        return sb.toString();
    }


    /**
     * 判断字符串是否为空
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param cs
     * @return
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);

    }

    private static final char DY = '\'';
    private static final char DH = ',';

    public static String makeSqlInString(String str) {
        String[] strs = str.split(",");
        StringBuilder sb = new StringBuilder();
        String field = null;
        for (int i = 0; i < strs.length; i++) {
            field = strs[i].trim();
            if (isNotBlank(field)) {
                sb.append(DY);
                sb.append(field);
                sb.append(DY);
                if (i < strs.length - 1) {
                    sb.append(DH);
                }
            }
        }
        return sb.toString();
    }
}