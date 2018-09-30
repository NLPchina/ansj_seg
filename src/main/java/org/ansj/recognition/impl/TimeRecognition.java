package org.ansj.recognition.impl;

import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间识别抽取
 *
 * @author sunyang
 */
public class TimeRecognition implements Recognition {

    private static final long serialVersionUID = 1L;
    private static final Nature nature = new Nature("t");
    private static final String NUM = Matcher.quoteReplacement("[０１２３４５６７８９\\d]");
    private static final String TIME_SFM = Matcher.quoteReplacement(
            "NUM{1,2}[点时點時]?(:?NUM{1,2}分?(:?NUM{1,2}秒?)?)?[ \t]*(PM|AM)?");
    private static final String TIME_APM = Matcher.quoteReplacement("上午|下午|中午|晚上?");

    private static final String[] TIME_PATTERNS = new String[]{"NUM{1,4}年NUM{1,2}月NUM{1,2}[日号](TIME_APM)?([ \t]*TIME_SFM)?",
            "NUM{4}-NUM{2}-NUM{2}(TIME_APM)?([ \t]*TIME_SFM)?",
            "NUM{1,4}年(正|一|二|三|四|五|六|七|八|九|十|十一|十二|腊)月(初|十|二十|三十)[ 一二三四五六七八九十](TIME_APM)?([ \t]*TIME_SFM)?",
            //以上都为年月日，后可跟上时分秒

            "(NUM{1,2}月份?)?NUM{1,2}[日号]?(TIME_APM)?([ \t]*TIME_SFM)?",
            "((正|一|二|三|四|五|六|七|八|九|十|十一|十二|腊)月)?(初|十|二十|三十)[ 一二三四五六七八九十](TIME_APM)?([ \t]*TIME_SFM)?",
            "(昨天|昨日|昨日上午|昨日下午|昨日晚上|昨天早上|昨天上午|昨天中午|昨天下午|昨晚|昨夜|昨天晚上|今天早上|今天上午|今天下午|今晚|今天晚上|今日上午|今日下午|今日|今天|前天|今年|去年|当日|当日上午|上午|下午|中午|清晨|前晚|早上|凌晨|今晨|近日|日前|不久前)(TIME_APM)?([ \t]*TIME_SFM)?",
            "星期[一|二|三|四|五|六|天|日](TIME_APM)?([ \t]*TIME_SFM)?",
            //以上都为精确到日的正则，后可跟上时分秒

            "(NUM{1,4}年)?(NUM{1,2}月|(正|一|二|三|四|五|六|七|八|九|十|十一|十二|腊)月)",//某年某月
            "NUM{1,4}年",//某年
            "(TIME_APM)?[ \t]*TIME_SFM",//时分秒
            "[^ “\"](1|2|3|4|5|6|7|8|9|10|11|12)[·.-:]NUM{1,2}[”\" $]"};//时-分

//    private static final Pattern TIME_PATTERN_ALL = Pattern
//            .compile(
//                    "((\\d|[０１２３４５６７８９]){1,4}年(\\d|[０１２３４５６７８９]){1,2}月(\\d|[０１２３４５６７８９]){1,2}[日|号](上午|下午|中午|晚)?(\\s)*((\\d|[０１２３４５６７８９]){1,2}([点|时|點|時])?((:)?(\\d|[０１２３４５６７８９]){1,2}(分)?((:)?(\\d|[０１２３４５６７８９]){1,2}(秒)?)?)?)?(\\s)*(PM|AM)?|(\\d|[０１２３４５６７８９]){1,2}(月|月份)(\\d|[０１２３４５６７８９]){1,2}([日|号])?(上午|下午|中午|晚)?(\\s)*((\\d|[０１２３４５６７８９]){1,2}([点|时|點|時])?((:)?(\\d|[０１２３４５６７８９]){1,2}(分)?((:)?(\\d|[０１２３４５６７８９]){1,2}(秒)?)?)?)?(\\s)*(PM|AM)?|(\\d|[０１２３４５６７８９]){1,2}日(上午|下午|中午|晚)?(\\s)*((\\d|[０１２３４５６７８９]){1,2}([点|时|點|時])?((:)?(\\d|[０１２３４５６７８９]){1,2}(分)?((:)?(\\d|[０１２３４５６７８９]){1,2}(秒)?)?)?)?(\\s)*(PM|AM)?|(昨天|昨日|昨日上午|昨日下午|昨日晚上|昨天早上|昨天上午|昨天中午|昨天下午|昨晚|昨夜|昨天晚上|今天早上|今天上午|今天下午|今晚|今天晚上|今日上午|今日下午|今日|今天|前天|今年|去年|当日|当日上午|上午|下午|中午|清晨|前晚|早上|凌晨|今晨|近日|日前|不久前)((\\d|[０１２３４５６７８９]){1,2}[点|时|點|時])?((:)?(\\d|[０１２３４５６７８９]){1,2}(分)?((:)?(\\d|[０１２３４５６７８９]){1,2}(秒)?)?)?(\\s)*(PM|AM)?|[\\“|\"](1|2|3|4|5|6|7|8|9|10|11|12)[·|.| |-](\\d|[０１２３４５６７８９]){1,2}[\\”|\"]|星期[一|二|三|四|五|六|天|日]|(\\d|[０１２３４５６７８９]){1,2}[点|时|點|時]((:)?(\\d|[０１２３４５６７８９]){1,2}(分)?((:)?(\\d|[０１２３４５６７８９]){1,2}(秒)?)?)?(\\s)*(PM|AM)?|(\\d|[０１２３４５６７８９]){4}年((\\d|[０１２３４５６７８９]){1,2}月)?|(\\d|[０１２３４５６７８９]){1,2}月|(正|一|二|三|四|五|六|七|八|九|十|十一|十二|腊)月((初|十|二十|三十)[ 一二三四五六七八九十])?(上午|下午|中午|晚)?|((\\d|[０１２３４５６７８９]){4}-(\\d|[０１２３４５６７８９]){2}-(\\d|[０１２３４５６７８９]){2})?(\\s)*(\\d|[０１２３４５６７８９]){2}:(\\d|[０１２３４５６７８９]){2}:(\\d|[０１２３４５６７８９]){2}|(\\d|[０１２３４５６７８９]){4}-(\\d|[０１２３４５６７８９]){2}-(\\d|[０１２３４５６７８９]){2}(\\s)*((\\d|[０１２３４５６７８９]){2}:(\\d|[０１２３４５６７８９]){2}:(\\d|[０１２３４５６７８９]){2})?)",
//                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private static Pattern TIME_PATTERN_ALL;
    private static final HashSet<String> TIME_START = new HashSet<>();

    static {
        StringBuilder allPat = new StringBuilder();
        for (String s : TIME_PATTERNS) {
            allPat.append('(').append(
                    s.replaceAll("TIME_APM", TIME_APM).replaceAll("TIME_SFM", TIME_SFM).replaceAll("NUM", NUM))
                    .append(")|");
        }
        TIME_PATTERN_ALL = Pattern.compile(allPat.substring(0, allPat.length() - 1));
        for (int i = 0; i < 10; i++) {
            TIME_START.add(String.valueOf(i));
        }
        for (char c : "０１２３４５６７８９昨今前去当正一二三四五六七八九十".toCharArray()) {
            TIME_START.add(String.valueOf(c));
        }
        TIME_START.addAll(Arrays.asList("上午|下午|中午|清晨|早上|凌晨|近日|日前|不久前|星期".split("\\|")));
    }

    @Override
    public void recognition(Result result) {
        if (result.getTerms().isEmpty()) {
            return;
        }
        StringBuilder timeWord = new StringBuilder();
        List<Term> terms = result.getTerms();
        LinkedList<Term> mergeList = new LinkedList<Term>();
        List<Term> list = new LinkedList<Term>();

        for (int i = 0; i < terms.size(); i++) {
            boolean isTime = false;
            Term termBase = terms.get(i);
            int timeTermsLength = 1;
            int matchLength = 0; //匹配长度
            boolean isStartedWithTime = false;
            String cname = termBase.getName();
            for (int l = 0, ml = Math.min(3, cname.length()); l < ml; l++) {
                if (TIME_START.contains(cname.substring(0, l))) {
                    isStartedWithTime = true;
                    break;
                }
            }
            if (isStartedWithTime) {
                for (int j = i; j < terms.size() && matchLength < 11; j++) { //向后最大找14个词匹配是否是时间词
                    Term term = terms.get(j);
                    String name = term.getName();
                    timeWord.append(name);
                    Matcher matcher = TIME_PATTERN_ALL.matcher(timeWord);
                    mergeList.add(term);
                    if (matcher.matches()) {
                        isTime = true;
                        timeTermsLength += (j - i);
                        i = j;
                    }
                    matchLength++;
                }
            }
            if (isTime) {
                Term ft = mergeList.pollFirst();
                if (ft != null) {
                    for (int k = 0; k < timeTermsLength - 1; k++) {
                        ft.merageWithBlank(mergeList.get(k));
                    }
                    ft.setNature(nature);
                }
                list.add(ft);
            } else {
                list.add(termBase);
            }
            mergeList.clear();
            timeWord.delete(0, timeWord.length());

        }
        result.setTerms(list);
    }


}
