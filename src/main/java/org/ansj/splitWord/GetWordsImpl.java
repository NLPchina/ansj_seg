package org.ansj.splitWord;

import org.ansj.AnsjItem;
import org.ansj.library.CoreDictionary;

import static org.ansj.AnsjContext.CONTEXT;

public class GetWordsImpl implements GetWords {

    /**
     * offe : 当前词的偏移量
     */
    public int offe;

    /**
     * 构造方法，同时加载词典,传入词语相当于同时调用了setStr() ;
     */
    public GetWordsImpl(String str) {
        setStr(str);
    }

    /**
     * 构造方法，同时加载词典
     */
    public GetWordsImpl() {
    }

    int charsLength = 0;

    @Override
    public void setStr(String str) {
        setChars(str.toCharArray(), 0, str.length());
    }

    @Override
    public void setChars(char[] chars, int start, int end) {
        this.chars = chars;
        i = start;
        this.start = start;
        charsLength = end;
        checkValue = 0;
    }

    public char[] chars;
    private int charHashCode;
    private int start = 0;
    public int end = 0;
    private int baseValue = 0;
    private int checkValue = 0;
    private int tempBaseValue = 0;
    public int i = 0;
    private String str = null;

    @Override
    public String allWords() {
        for (; i < charsLength; i++) {
            charHashCode = chars[i];
            end++;
            switch (getStatement()) {
                case 0:
                    if (baseValue == chars[i]) {
                        str = String.valueOf(chars[i]);
                        offe = i;
                        start = ++i;
                        end = 0;
                        baseValue = 0;
                        tempBaseValue = baseValue;
                        return str;
                    } else {
                        i = start;
                        start++;
                        end = 0;
                        baseValue = 0;
                        break;
                    }
                case 2:
                    i++;
                    offe = start;
                    tempBaseValue = baseValue;
                    return CONTEXT().coreDictionary.getItem(tempBaseValue).getName();
                case 3:
                    offe = start;
                    start++;
                    i = start;
                    end = 0;
                    tempBaseValue = baseValue;
                    baseValue = 0;
                    return CONTEXT().coreDictionary.getItem(tempBaseValue).getName();
            }

        }
        if (start++ != i) {
            i = start;
            baseValue = 0;
            return allWords();
        }
        end = 0;
        baseValue = 0;
        i = 0;
        return null;
    }

    /**
     * 根据用户传入的c得到单词的状态. 0.代表这个字不在词典中 1.继续 2.是个词但是还可以继续 3.停止已经是个词了
     *
     * @return
     */
    private int getStatement() {
        final CoreDictionary dat = CONTEXT().coreDictionary;

        checkValue = baseValue;
        baseValue = dat.getItem(checkValue).getBase() + charHashCode;
        if (baseValue < dat.arrayLength && (dat.getItem(baseValue).getCheck() == checkValue || dat.getItem(baseValue).getCheck() == -1)) {
            return dat.getItem(baseValue).getStatus();
        }
        return 0;
    }

    public AnsjItem getItem() {
        return CONTEXT().coreDictionary.getItem(tempBaseValue);
    }

    @Override
    public int getOffe() {
        return offe;
    }

}