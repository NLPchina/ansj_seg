package org.ansj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.experimental.Wither;
import org.ansj.crf.Model;
import org.ansj.crf.ModelSerializer;
import org.ansj.crf.SplitWord;
import org.ansj.library.*;
import org.nlpcn.commons.lang.dat.DoubleArrayTire;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;
import org.nlpcn.commons.lang.util.FileFinder;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static com.google.common.base.Charsets.UTF_8;
import static java.lang.Boolean.parseBoolean;
import static lombok.AccessLevel.PRIVATE;
import static org.ansj.AnsjUtils.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@AllArgsConstructor(access = PRIVATE)
public class AnsjContext {

//    public static void main(String[] args) {
//        System.out.println(Math.log(dTemp * 2));
//    }

    public static final Logger LIBRARYLOG = Logger.getLogger("DICLOG");

    public static final String TAB = "\t";
    public static final String NEW_LINE = "\n";

    // 平滑参数
    public static final double dSmoothingPara = 0.1;
    // 一个参数
    public static final int MAX_FREQUENCE = 2079997;// 7528283+329805;
    // ﻿Two linked Words frequency
    public static final double dTemp = (double) 1 / MAX_FREQUENCE;

    //
    private static volatile AnsjContext _INSTANCE;

    public static AnsjContext CONTEXT() {
        return _INSTANCE != null ? _INSTANCE : createIfNotExists();
    }

    @Synchronized
    private static AnsjContext createIfNotExists() {
        if (_INSTANCE != null) {
            return _INSTANCE;
        } else {
            _INSTANCE = new AnsjContext(properties());
            return _INSTANCE;
        }
    }

    @Synchronized
    public static void refreshContext(final AnsjContext context) {
        userLibraryRef.set(null);
        _INSTANCE = context;
    }

    // 是否开启人名识别
    @Wither
    public final boolean nameRecognition;

    // 是否开启数字识别
    @Wither
    public final boolean numRecognition;

    // 是否数字和量词合并
    @Wither
    public final boolean quantifierRecognition;

    @Wither
    public final boolean realName;
    /**
     * 是否用户辞典不加载相同的词
     */
    @Wither
    public final boolean skipUserDefine;
    /**
     * 用户自定义词典的加载,如果是路径就扫描路径下的dic文件
     */
    @Wither
    public final String userLibraryLocation;
    @Wither
    public final String userAmbiguityLibraryLocation;

    public final String crfModelLocation;


    public static final NatureLibrary natureLibrary;

    static {
        natureLibrary = new NatureLibrary( // 词性库必须事先初始化, 因为在TermNature的static字段使用了它
                rawLines(classpathResource("nature/nature.map")), // 词性表
                rawLines(classpathResource("nature/nature.table")) // 词性关联表
        );
    }

    public final CompanyAttrLibrary companyAttrLibrary;//机构名词典
    public final PersonAttrLibrary personAttrLibrary;
    public final CoreDictionary coreDictionary;
    public final NgramLibrary ngramLibrary;

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private AnsjContext(final ResourceBundle properties) {
        LIBRARYLOG.info("new AnsjContext");

        this.nameRecognition = parseBoolean(property(properties, "nameRecognition", "true"));
        this.numRecognition = parseBoolean(property(properties, "numRecognition", "true"));
        this.quantifierRecognition = parseBoolean(property(properties, "quantifierRecognition", "true"));
        //
        this.realName = parseBoolean(property(properties, "realName", "false"));
        this.skipUserDefine = parseBoolean(property(properties, "skipUserDefine", "false"));
        this.userLibraryLocation = property(properties, "userLibraryLocation", "library/default.dic");
        this.userAmbiguityLibraryLocation = property(properties, "userAmbiguityLibraryLocation", "library/ambiguity.dic");
        this.crfModelLocation = property(properties, "crfModel", "library/crf.bz2");
        //
        this.companyAttrLibrary = new CompanyAttrLibrary(
                rawLines(classpathResource("company/company.data"))
        );
        this.personAttrLibrary = new PersonAttrLibrary(
                rawLines(classpathResource("person/person.dic")),
                (Map<String, int[][]>) new ObjectInputStream(classpathResource("person/asian_name_freq.data")).readObject()//名字词性对象反序列化
        );
        final DoubleArrayTire coreDic = DoubleArrayTire.loadText(classpathResource("core.dic"), AnsjItem.class);
        this.coreDictionary = new CoreDictionary(this.personAttrLibrary, coreDic);
        this.ngramLibrary = new NgramLibrary(
                rawLines(classpathResource("bigramdict.dic"), UTF_8),
                this.coreDictionary
        );
    }

    private static final String DEFAULT_NATURE = "userDefine";
    private static final Integer DEFAULT_FREQ = 1000;
    public static final String DEFAULT_FREQ_STR = DEFAULT_FREQ.toString();

    private static final AtomicReference<Object> userLibraryRef = new AtomicReference<>();

    public UserLibrary getUserLibrary() {
        Object value = AnsjContext.userLibraryRef.get();
        if (value == null) {
            synchronized (AnsjContext.userLibraryRef) {
                value = AnsjContext.userLibraryRef.get();
                if (value == null) {
                    UserLibrary actualValue = this.initUserLibrary();
                    value = actualValue == null ? AnsjContext.userLibraryRef : actualValue;
                    AnsjContext.userLibraryRef.set(value);
                }
            }
        }
        return ((UserLibrary) (value == AnsjContext.userLibraryRef ? null : value));
    }

    UserLibrary initUserLibrary() {
        return new UserLibrary(this.loadUserDic(), this.loadUserAmbiguityDic());
    }

    /**
     * 加载用户词典,传入
     */
    public Forest loadUserDic() {
        final Forest forest = new Forest();
        for (final String line : rawLines(filesystemDics(this.userLibraryLocation))) {//单个文件加载词典
            if (isNotBlank(line)) {
                final String[] strs = line.split(TAB);
                strs[0] = strs[0].toLowerCase();
                // 如何核心辞典存在那么就放弃
                if (this.skipUserDefine && this.coreDictionary.getId(strs[0]) > 0) {
                    continue;
                }
                final Value value = strs.length != 3 ?
                        new Value(strs[0], DEFAULT_NATURE, DEFAULT_FREQ_STR) :
                        new Value(strs[0], strs[1], strs[2]);
                Library.insertWord(forest, value);
            }
        }
        LIBRARYLOG.info("load userLibrary ok!");
        return forest;
    }

    /**
     * 加载用户歧义修正词典
     */
    @SneakyThrows
    public Forest loadUserAmbiguityDic() {
        final InputStream inputStream = filesystemDic(this.userAmbiguityLibraryLocation);
        final Forest forest = inputStream != null ? Library.makeForest(inputStream) : new Forest();
        LIBRARYLOG.info("init ambiguityLibrary ok!");
        return forest;
    }

    @Getter(lazy = true)
    private final SplitWord crfSplitWord = initCrfSplitWord();// crf 模型

    /**
     * 得到默认的模型
     */
    @SneakyThrows
    SplitWord initCrfSplitWord() {
        long start = System.currentTimeMillis();
        LIBRARYLOG.info("begin init crf model!");
        final Model model = ModelSerializer.read(filesystemResource(this.crfModelLocation)).whithoutGrad();
        //final Model model = Model.loadModel(filesystemResource(this.crfModel));
        final SplitWord crfSplitWord = new SplitWord(model);
        LIBRARYLOG.info("load crf crf use time:" + (System.currentTimeMillis() - start));
        return crfSplitWord;
    }

    /**
     * @return 配置in文件
     */
    @SneakyThrows
    static ResourceBundle properties() {
        try {
            return ResourceBundle.getBundle("library");
        } catch (final RuntimeException e) {
            // continue try
        }
        final File found = FileFinder.find("library.properties");
        if (found != null) {
            final String file = found.getAbsolutePath();
            //LIBRARYLOG.info("library.properties not find in classPath! found at " + file + " make sure it is your config!");
            return new PropertyResourceBundle(reader(filesystemResource(file), System.getProperty("file.encoding")));
        }
        LIBRARYLOG.warning("library.properties not found use default config values.");
        return null;
    }

    static String property(final ResourceBundle properties, final String key, final String defaultValue) {
        return properties != null && properties.containsKey(key) ? properties.getString(key) : defaultValue;
    }

//    /**
//     * 机构名词典
//     */
//    public static BufferedReader getNewWordReader() {
//        return reader(classpathResource("newWord/new_word_freq.dic"));
//    }
//
//    /**
//     * 核心词典
//     */
//    public static BufferedReader getArraysReader() {
//        return reader(classpathResource("arrays.dic"));
//    }
//
//    /**
//     * 数字词典
//     */
//    public static BufferedReader getNumberReader() {
//        return reader(classpathResource("numberLibrary.dic"));
//    }
//
//    /**
//     * 英文词典
//     */
//    public static BufferedReader getEnglishReader() {
//        return reader(classpathResource("englishLibrary.dic"));
//    }
}
