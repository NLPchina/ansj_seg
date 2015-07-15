package org.ansj.library;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Branch;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.domain.WoodInterface;
import org.nlpcn.commons.lang.tire.library.Library;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicReference;

import static org.ansj.util.MyStaticValue.LIBRARYLOG;
import static org.nlpcn.commons.lang.util.StringUtil.isBlank;


/**
 * 用户自定义词典操作类
 *
 * @author ansj
 */
public final class UserDefineLibrary {

    private static final AtomicReference<UserDefineLibrary> _INSTANCE_REF = new AtomicReference<>();

    @Synchronized
    private static UserDefineLibrary newInstance(final Forest forest, final Forest ambiguityForest) {
        return new UserDefineLibrary(forest, ambiguityForest);
    }

    public static UserDefineLibrary getInstance(final Forest forest, final Forest ambiguityForest) {
        final UserDefineLibrary current = _INSTANCE_REF.get();
        if (current != null) {
            return current;
        } else {
            final UserDefineLibrary newInstance = newInstance(forest, ambiguityForest);
            _INSTANCE_REF.set(newInstance);
            return newInstance;
        }
    }

    public static UserDefineLibrary getInstance() {
        return getInstance(null, null);
    }

    private static final String DEFAULT_NATURE = "userDefine";

    private static final Integer DEFAULT_FREQ = 1000;

    public static final String DEFAULT_FREQ_STR = DEFAULT_FREQ.toString();

    @Getter
    private final Forest forest;

    @Getter
    private final Forest ambiguityForest;

    private UserDefineLibrary(final Forest forest, final Forest ambiguityForest) {
        // 加载用户自定义词典和补充词典
        this.forest = forest != null ?
                forest :
                loadLibrary(new Forest(), MyStaticValue.userLibrary, MyStaticValue.isSkipUserDefine);

        this.ambiguityForest = ambiguityForest != null ?
                ambiguityForest :
                initAmbiguityLibrary(MyStaticValue.ambiguityLibrary);
    }

    public GetWord getWord(final char[] chars) {
        return this.ambiguityForest != null ? new GetWord(this.ambiguityForest, chars) : null;
    }

    /**
     * 关键词增加
     *
     * @param keyword 所要增加的关键词
     * @param nature  关键词的词性
     * @param freq    关键词的词频
     */
    public void insertWord(final String keyword, final String nature, final int freq) {
        Library.insertWord(this.forest, new Value(keyword, nature, String.valueOf(freq)));
    }

    /**
     * 删除关键词
     */
    public void removeWord(final String word) {
        Library.removeWord(this.forest, word);
    }

    /**
     * 将用户自定义词典清空
     */
    public void clear() {
        this.forest.clear();
    }

    public boolean contains(final String word) {
        return getParams(this.forest, word) != null;
    }

    public String[] getParams(final String word) {
        return getParams(this.forest, word);
    }

    public static String[] getParams(final Forest forest, final String word) {
        WoodInterface<String[], Branch> temp = forest;
        for (final char ch : word.toCharArray()) {
            temp = temp.getBranch(ch);
            if (temp == null) {
                return null;
            }
        }
        return temp.getStatus() > 1 ? temp.getParam() : null;
    }

    /**
     * 加载词典,传入一本词典的路径.或者目录.词典后缀必须为.dic
     */
    public static Forest loadLibrary(final Forest forest, final String path, final boolean isSkipUserDefine) {
        if (path == null) {
            return forest;
        }
        // 加载用户自定义词典
        final File file = new File(path);
        if (!file.canRead() || file.isHidden()) {
            LIBRARYLOG.warning("init userLibrary  warning :" + file.getAbsolutePath() + " because : file not found or failed to read !");
            return forest;
        }

        final File[] files = file.isFile() ? new File[]{file} : (file.isDirectory() ? file.listFiles() : null);
        if (files == null || files.length == 0) {
            LIBRARYLOG.warning("init user library  error :" + file.getAbsolutePath() + " because : not find that file !");
            return forest;
        }
        for (final File f : files) {
            if (f.getName().trim().endsWith(".dic")) {
                loadFile(forest, f, isSkipUserDefine);
            }
        }
        return forest;
    }

    /**
     * 加载纠正词典
     */
    private static Forest initAmbiguityLibrary(final String path) {
        if (isBlank(path)) {
            LIBRARYLOG.warning("init ambiguity  warning :" + path + " because : file not found or failed to read !");
            return null;
        }
        final File file = new File(path);
        if (!file.isFile() || !file.canRead()) {
            LIBRARYLOG.warning("init ambiguity  warning :" + file.getAbsolutePath() + " because : file not found or failed to read !");
            return null;
        }
        try {
            final Forest result = Library.makeForest(path);
            LIBRARYLOG.info("init ambiguityLibrary ok!");
            return result;
        } catch (final Exception e) {
            LIBRARYLOG.warning("init ambiguity  error :" + file.getAbsolutePath() + " because : not find that file or can not to read !");
            return null;
        }
    }

    // 单个文件加载词典
    @SneakyThrows
    public static void loadFile(final Forest forest, final File file, final boolean isSkipUserDefine) {
        if (!file.canRead()) {
            LIBRARYLOG.warning("file in path " + file.getAbsolutePath() + " can not to read!");
            return;
        }

        String temp;
        try (final BufferedReader br = IOUtil.getReader(new FileInputStream(file), "UTF-8")) {
            while ((temp = br.readLine()) != null) {
                if (!isBlank(temp)) {
                    final String[] strs = temp.split("\t");
                    strs[0] = strs[0].toLowerCase();

                    // 如何核心辞典存在那么就放弃
                    if (isSkipUserDefine && DATDictionary.getId(strs[0]) > 0) {
                        continue;
                    }

                    final Value value = strs.length != 3 ?
                            new Value(strs[0], DEFAULT_NATURE, DEFAULT_FREQ_STR) :
                            new Value(strs[0], strs[1], strs[2]);
                    Library.insertWord(forest, value);
                }
            }
            LIBRARYLOG.info("init user userLibrary ok path is : " + file.getAbsolutePath());
        }
    }
}
