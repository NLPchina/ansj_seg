package org.ansj.util;

import lombok.SneakyThrows;
import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.ansj.domain.AnsjItem;
import org.ansj.domain.Nature;
import org.ansj.library.*;
import org.nlpcn.commons.lang.dat.DoubleArrayTire;
import org.nlpcn.commons.lang.util.FileFinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import static com.google.common.base.Charsets.UTF_8;
import static org.ansj.util.AnsjUtils.*;

/**
 * 这个类储存一些公用变量.
 *
 * @author ansj
 */
public class MyStaticValue {

    // 平滑参数
    public static final double dSmoothingPara = 0.1;
    // 一个参数
    public static final int MAX_FREQUENCE = 2079997;// 7528283+329805;
    // ﻿Two linked Words frequency
    public static final double dTemp = (double) 1 / MAX_FREQUENCE;

//    public static void main(String[] args) {
//        System.out.println(Math.log(dTemp * 2));
//    }


    public static final String TAB = "\t";
    public static final String NEW_LINE = "\n";

    public static final Logger LIBRARYLOG = Logger.getLogger("DICLOG");

    // 是否开启人名识别
    public static boolean isNameRecognition = true;

    private static final Lock LOCK = new ReentrantLock();

    // 是否开启数字识别
    public static boolean isNumRecognition = true;

    // 是否数字和量词合并
    public static boolean isQuantifierRecognition = true;

    // crf 模型

    private static SplitWord crfSplitWord = null;

    public static boolean isRealName = false;

    /**
     * 用户自定义词典的加载,如果是路径就扫描路径下的dic文件
     */
    public static String userLibrary = "library/default.dic";

    public static String ambiguityLibrary = "library/ambiguity.dic";

    public static String crfModel = "library/crf.model";

    /**
     * 是否用户辞典不加载相同的词
     */
    public static boolean isSkipUserDefine = false;

    public static Nature NATURE_NW() {
        return NATURE_LIBRARY.getNature("nw");
    }

    public static Nature NATURE_NRF() {
        return NATURE_LIBRARY.getNature("nrf");
    }

    public static Nature NATURE_NR() {
        return NATURE_LIBRARY.getNature("nr");
    }

    public static Nature NATURE_NULL() {
        return NATURE_LIBRARY.getNature("null");
    }

    public static final NatureLibrary NATURE_LIBRARY = new NatureLibrary(
            AnsjUtils.rawLinesFromClasspath("nature/nature.map"), // 词性表
            AnsjUtils.rawLinesFromClasspath("nature/nature.table") // 词性关联表
    );

    /**
     * 机构名词典
     */
    public static final CompanyAttrLibrary COMPANY_ATTR_LIBRARY = new CompanyAttrLibrary(
            AnsjUtils.rawLinesFromClasspath("company/company.data")
    );

    public static final PersonAttrLibrary PERSON_ATTR_LIBRARY = PERSON_ATTR_LIBRARY();

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static PersonAttrLibrary PERSON_ATTR_LIBRARY() {
        return new PersonAttrLibrary(
                AnsjUtils.rawLinesFromClasspath("person/person.dic"),
                (Map<String, int[][]>) new ObjectInputStream(AnsjUtils.classpathResource("person/asian_name_freq.data")).readObject()//名字词性对象反序列化
        );
    }

    private static final DoubleArrayTire CORE_DIC = DoubleArrayTire.loadText(AnsjUtils.classpathResource("core.dic"), AnsjItem.class);
    public static final DATDictionary DAT_DICTIONARY = new DATDictionary(CORE_DIC);

    public static final NgramLibrary NGRAM_LIBRARY = new NgramLibrary(
            rawLinesFromClasspath("bigramdict.dic", UTF_8),
            DAT_DICTIONARY
    );

    static {
        init();
    }

    @SneakyThrows
    private static void init() {
        /**
         * 配置文件变量
         */
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle("library");
        } catch (Exception e) {
            try {
                File find = FileFinder.find("library.properties");
                if (find != null) {
                    rb = new PropertyResourceBundle(reader(filesystemResource(find.getAbsolutePath()), System.getProperty("file.encoding")));
                    LIBRARYLOG.info("load library not find in classPath ! i find it in " + find.getAbsolutePath() + " make sure it is your config!");
                }
            } catch (Exception e1) {
                LIBRARYLOG.warning("not find library.properties. and err " + e.getMessage() + " i think it is a bug!");
            }
        }

        if (rb == null) {
            LIBRARYLOG.warning("not find library.properties in classpath use it by default !");
        } else {
            if (rb.containsKey("userLibrary"))
                userLibrary = rb.getString("userLibrary");
            if (rb.containsKey("ambiguityLibrary"))
                ambiguityLibrary = rb.getString("ambiguityLibrary");
            if (rb.containsKey("isSkipUserDefine"))
                isSkipUserDefine = Boolean.valueOf(rb.getString("isSkipUserDefine"));
            if (rb.containsKey("isRealName"))
                isRealName = Boolean.valueOf(rb.getString("isRealName"));
            if (rb.containsKey("crfModel"))
                crfModel = rb.getString("crfModel");
        }
    }

    /**
     * 机构名词典
     */
    public static BufferedReader getNewWordReader() {
        return reader(classpathResource("newWord/new_word_freq.dic"));
    }

    /**
     * 核心词典
     */
    public static BufferedReader getArraysReader() {
        return reader(classpathResource("arrays.dic"));
    }

    /**
     * 数字词典
     */
    public static BufferedReader getNumberReader() {
        return reader(classpathResource("numberLibrary.dic"));
    }

    /**
     * 英文词典
     */
    public static BufferedReader getEnglishReader() {
        return reader(classpathResource("englishLibrary.dic"));
    }

    /**
     * 得到默认的模型
     */
    @SneakyThrows
    public static SplitWord getCRFSplitWord() {
        if (crfSplitWord != null) {
            return crfSplitWord;
        }
        LOCK.lock();
        if (crfSplitWord != null) {
            return crfSplitWord;
        }

        try {
            long start = System.currentTimeMillis();
            LIBRARYLOG.info("begin init crf model!");
            crfSplitWord = new SplitWord(Model.loadModel(filesystemResource(crfModel)));
            LIBRARYLOG.info("load crf crf use time:" + (System.currentTimeMillis() - start));
        } finally {
            LOCK.unlock();
        }

        return crfSplitWord;
    }
}
