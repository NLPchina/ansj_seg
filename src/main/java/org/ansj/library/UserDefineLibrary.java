package org.ansj.library;

import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

import java.io.*;
import java.net.URL;

import static org.ansj.util.MyStaticValue.LIBRARYLOG;

/**
 * 用户自定义词典操作类
 *
 * @author ansj
 */
public class UserDefineLibrary {

    public static final String DEFAULT_NATURE = "userDefine";

    public static final Integer DEFAULT_FREQ = 1000;

    public static final String DEFAULT_FREQ_STR = "1000";

    public static Forest FOREST = null;

    public static Forest ambiguityForest = null;
    public static Forest synonymsForest = null;

    static {
        initUserLibrary();
        initAmbiguityLibrary();
        initSynonymsLibrary();
    }

    /**
     * 关键词增加
     *
     * @param keyword 所要增加的关键词
     * @param nature  关键词的词性
     * @param freq    关键词的词频
     */
    public static void insertWord(String keyword, String nature, int freq) {
        if (FOREST == null) {
            FOREST = new Forest();
        }
        String[] paramers = new String[2];
        paramers[0] = nature;
        paramers[1] = String.valueOf(freq);
        Value value = new Value(keyword, paramers);
        Library.insertWord(FOREST, value);
    }

    /**
     * 增加关键词
     *
     * @param keyword
     */
    public static void insertWord(String keyword) {
        insertWord(keyword, DEFAULT_NATURE, DEFAULT_FREQ);
    }


    /**
     * 加载纠正词典
     */
    private static void initAmbiguityLibrary() {

        File[] lib = findLibrary(MyStaticValue.ambiguityLibrary);

        if (lib.length > 0) {
            ambiguityForest = new Forest();
            for (File file : lib) {
                try (BufferedReader br = IOUtil.getReader(file, "utf-8")) {
                    String temp;
                    while ((temp = br.readLine()) != null) {
                        if (StringUtil.isNotBlank(temp)) {
                            temp = StringUtil.trim(temp);
                            String[] split = temp.split("\t");
                            StringBuilder sb = new StringBuilder();
                            if (split.length % 2 != 0) {
                                LIBRARYLOG.error("init ambiguity  error in line :" + temp + " format err !");
                            }
                            for (int i = 0; i < split.length; i += 2) {
                                sb.append(split[i]);
                            }
                            ambiguityForest.addBranch(sb.toString(), split);
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    LIBRARYLOG.warn("不支持的编码", e);
                } catch (IOException e) {
                    LIBRARYLOG.warn("Init ambiguity library error :{}, path: {}", e.getMessage(), file.getPath());
                }
            }

            LIBRARYLOG.info("Init ambiguity library ok!");

        } else {
            LIBRARYLOG.warn("Init ambiguity library warning :{} because : file not found or failed to read !", MyStaticValue.ambiguityLibrary);
        }

    }
    
    /**
     * 初始化同义词词典
     */
    private static void initSynonymsLibrary() {

        File[] lib = findLibrary(MyStaticValue.synonymsLibrary);

        if (lib.length > 0) {
        	synonymsForest = new Forest();
            for (File file : lib) {
                try (BufferedReader br = IOUtil.getReader(file, "utf-8")) {
                    String temp;
                    while ((temp = br.readLine()) != null) {
                        if (StringUtil.isNotBlank(temp)) {
                            temp = StringUtil.trim(temp);
                            String[] split = temp.split("\t");
                            LIBRARYLOG.info("init synonyms in line :" + temp);
                            synonymsForest.addBranch(split[0], split);
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    LIBRARYLOG.warn("不支持的编码", e);
                } catch (IOException e) {
                    LIBRARYLOG.warn("Init synonyms library error :{}, path: {}", e.getMessage(), file.getPath());
                }
            }

            LIBRARYLOG.info("Init synonyms library ok!");

        } else {
            LIBRARYLOG.warn("Init synonyms library warning :{} because : file not found or failed to read !", MyStaticValue.synonymsLibrary);
        }

    }

    /**
     * 加载用户自定义词典和补充词典
     */
    private static void initUserLibrary() {
        FOREST = MyStaticValue.getDicForest();
    }


    /**
     * 加载词典,传入一本词典的路径.或者目录.词典后缀必须为.dic 按文件名称顺序加载
     */
    public static void loadLibrary(Forest forest, String path) {

        File[] lib = findLibrary(path);

        if (lib.length > 0) {
            for (File file : lib) {
                String temp;
                String[] strs;
                Value value;
                try (BufferedReader br = IOUtil.getReader(new FileInputStream(file), "UTF-8")) {
                    while ((temp = br.readLine()) != null) {
                        if (StringUtil.isNotBlank(temp)) {
                            temp = StringUtil.trim(temp);
                            strs = temp.split("\t");
                            strs[0] = strs[0].toLowerCase();
                            // 如何核心辞典存在那么就放弃
                            if (MyStaticValue.isSkipUserDefine && DATDictionary.getId(strs[0]) > 0) {
                                continue;
                            }
                            if (strs.length != 3) {
                                value = new Value(strs[0], DEFAULT_NATURE, DEFAULT_FREQ_STR);
                            } else {
                                value = new Value(strs[0], strs[1], strs[2]);
                            }
                            Library.insertWord(forest, value);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    LIBRARYLOG.warn("不支持的编码", e);
                } catch (IOException e) {
                    LIBRARYLOG.warn("Init user library error :{}, path: {}", e.getMessage(), file.getPath());
                }
            }

            LIBRARYLOG.info("Init user library ok!");


        } else {
            LIBRARYLOG.warn("Init user library  error :{} because : not find that file !", path);
        }

    }

    /**
     * 删除关键词
     */
    public static void removeWord(String word) {
        Library.removeWord(FOREST, word);
    }

    public static String[] getParams(String word) {
        return getParams(FOREST, word);
    }

    public static String[] getParams(Forest forest, String word) {
        SmartForest<String[]> temp = forest;
        for (int i = 0; i < word.length(); i++) {
            temp = temp.get(word.charAt(i));
            if (temp == null) {
                return null;
            }
        }
        if (temp.getStatus() > 1) {
            return temp.getParam();
        } else {
            return null;
        }
    }

    public static boolean contains(String word) {
        return getParams(word) != null;
    }

    /**
     * 将用户自定义词典清空
     */
    public static void clear() {
        FOREST.clear();
    }


    /**
     * Load files
     *
     * @param path file path
     * @return File Array
     */
    private static File[] findLibrary(String path) {
        File[] libs = new File[0];
        File file = new File(path);
        if (!file.exists()) {
            // Try load from classpath
            URL url = UserDefineLibrary.class.getClassLoader().getResource(path);
            if (url != null) {
                file = new File(url.getPath());
            }
        }

        if (file.canRead()) {

            if (file.isFile()) {
                libs = new File[1];
                libs[0] = file;
            } else if (file.isDirectory()) {
                File[] files = file.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.endsWith(".dic") && dir.canRead()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                if (files != null && files.length > 0) {
                    libs = files;
                }
            }
        }
        return libs;
    }

}
