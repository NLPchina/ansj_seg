package org.ansj.splitWord;

public interface GetWords {

    /**
     * 全文全词全匹配
     *
     * @return 返还分完词后的句子
     */
    String allWords();

    /**
     * 同一个对象传入词语
     *
     * @param temp 传入的句子
     */
    void setStr(String temp);

    void setChars(char[] chars, int start, int end);

    int getOffe();
}
