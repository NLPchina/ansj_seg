package org.ansj;

import com.google.common.collect.ImmutableList;
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
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.google.common.base.Charsets.UTF_8;
import static java.lang.Boolean.parseBoolean;
import static java.util.Arrays.asList;
import static lombok.AccessLevel.PRIVATE;
import static org.ansj.AnsjUtils.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@AllArgsConstructor(access = PRIVATE)
public class AnsjContext {

//    public static void main(String[] args) {
//        System.out.println(Math.log(dTemp * 2));
//    }

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
        return _INSTANCE != null ? _INSTANCE : newContextIfNoOneExists();
    }

    @Synchronized
    private static AnsjContext newContextIfNoOneExists() {
        if (_INSTANCE != null) {
            return _INSTANCE;
        } else {
            _INSTANCE = new AnsjContext(properties());
            return _INSTANCE;
        }
    }

    @Synchronized
    public static void replaceContext(final AnsjContext context) {
        _INSTANCE = context;
    }
    //

    public static final Logger LIBRARYLOG = Logger.getLogger("DICLOG");

    // 是否开启人名识别
    public final boolean isNameRecognition;

    // 是否开启数字识别
    public final boolean isNumRecognition;

    // 是否数字和量词合并
    public final boolean isQuantifierRecognition;

    @Wither
    public final boolean isRealName;
    /**
     * 是否用户辞典不加载相同的词
     */
    @Wither
    public final boolean isSkipUserDefine;
    /**
     * 用户自定义词典的加载,如果是路径就扫描路径下的dic文件
     */
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

        this.isNameRecognition = parseBoolean(property(properties, "isNameRecognition", "true"));
        this.isNumRecognition = parseBoolean(property(properties, "isNumRecognition", "true"));
        this.isQuantifierRecognition = parseBoolean(property(properties, "isQuantifierRecognition", "true"));
        //
        this.isRealName = parseBoolean(property(properties, "isRealName", "false"));
        this.isSkipUserDefine = parseBoolean(property(properties, "isSkipUserDefine", "false"));
        this.userLibraryLocation = property(properties, "userLibrary", "library/default.dic");
        this.userAmbiguityLibraryLocation = property(properties, "userAmbiguityLibrary", "library/ambiguity.dic");
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

    /**
     * 加载用户词典,传入
     */
    public Forest loadUserDic() {
        final Forest forest = new Forest();
        for (final String dic : this.userLibraryResources()) {
            //LIBRARYLOG.warning("file in path " + dic + " can not to read!");
            for (final String line : rawLines(filesystemResource(dic))) {//单个文件加载词典
                if (isNotBlank(line)) {
                    final String[] strs = line.split(TAB);
                    strs[0] = strs[0].toLowerCase();

                    // 如何核心辞典存在那么就放弃
                    if (this.isSkipUserDefine && this.coreDictionary.getId(strs[0]) > 0) {
                        continue;
                    }

                    final Value value = strs.length != 3 ?
                            new Value(strs[0], DEFAULT_NATURE, DEFAULT_FREQ_STR) :
                            new Value(strs[0], strs[1], strs[2]);
                    Library.insertWord(forest, value);
                }
            }
            LIBRARYLOG.info("load userLibrary ok path is : " + dic);
        }
        LIBRARYLOG.info("load userLibrary all ok!");
        return forest;
    }

    public List<String> userLibraryResources() { // 处理词典的路径.或者目录.词典后缀必须为.dic
        final String path = this.userLibraryLocation;
        if (path == null || !new File(path).canRead() || new File(path).isHidden()) {
            LIBRARYLOG.warning("init userLibrary  warning :" + path + " because : file not found or failed to read!");
            return ImmutableList.of();
        }
        // 加载用户自定义词典
        final File file = new File(path);
        final File[] files = file.isFile() ? new File[]{file} : (file.isDirectory() ? file.listFiles() : new File[0]);
        if (files == null || files.length == 0) {
            LIBRARYLOG.warning("init user library  error :" + path + " because : not find that file !");
            return ImmutableList.of();
        }
        return asList(files)
                .stream()
                .filter(f -> f.getName().trim().endsWith(".dic"))
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
    }

    /**
     * 加载用户纠正词典
     */
    @SneakyThrows
    public Forest loadUserAmbiguityDic() {
        final String path = this.ambiguityLibraryResource();
        final Forest forest = isNotBlank(path) ? Library.makeForest(path) : new Forest();
        LIBRARYLOG.info("init ambiguityLibrary ok!");
        return forest;
    }

    public String ambiguityLibraryResource() {
        final String path = this.userAmbiguityLibraryLocation;
        if (isBlank(path) || !new File(path).isFile() || !new File(path).canRead()) {
            LIBRARYLOG.warning("init ambiguity  warning :" + path + " because : file not found or failed to read!");
            return null;
        }
        return new File(path).getAbsolutePath();
    }

    @Getter(lazy = true)
    private final UserLibrary userLibrary = initUserDefineLibrary();

    UserLibrary initUserDefineLibrary() {
        return new UserLibrary(this.loadUserDic(), this.loadUserAmbiguityDic());
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
