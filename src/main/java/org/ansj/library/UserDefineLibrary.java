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
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.ansj.util.MyStaticValue.LIBRARYLOG;

/**
 * 用户自定义词典操作类
 *
 * @author ansj
 */
public class UserDefineLibrary {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private static final int EOF = -1;

    public static final String DEFAULT_NATURE = "userDefine";

    public static final Integer DEFAULT_FREQ = 1000;

    public static final String DEFAULT_FREQ_STR = "1000";

    public static Forest FOREST = null;

    public static Forest ambiguityForest = null;

    static {
        initUserLibrary();
        initAmbiguityLibrary();
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
                    LIBRARYLOG.warn("Init ambiguity library error :" + e.getMessage() + ", path: " + file.getPath());
                }
            }

            LIBRARYLOG.info("Init ambiguity library ok!");

        } else {
            LIBRARYLOG.warn("Init ambiguity library warning :" + MyStaticValue.ambiguityLibrary + " because : file not found or failed to read !");
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
                    LIBRARYLOG.warn("Init user library error :" + e.getMessage() + ", path: " + file.getPath());
                }
            }

            LIBRARYLOG.info("Init user library ok!");


        } else {
            LIBRARYLOG.warn("Init user library  error :" + path + " because : not find that file !");
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
                if ("file".equals(url.getProtocol())) {//from file
                    file = new File(url.getPath());
                } else if ("jar".equals(url.getProtocol())) {//from jar
                    try {
                        file = loadFileFromJar(url);
                        if (file == null) return libs;
                    } catch (IOException e) {
                        LIBRARYLOG.warn("Can unzip dics to java.io.tmpdir, Error:" + e.getMessage());
                    }
                }
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

    private static File loadFileFromJar(URL url) throws IOException {
        final List<String> paths = new ArrayList<>();
        read(url, new InputStreamCallback() {
            @Override
            public void onFile(String name, InputStream is) throws IOException {
                paths.add(name);
            }
        });
        int size = paths.size();
        File file = null;
        if (size == 0) {
            return null;
        } else if (size == 1) {
            File[] files = createTempFile(paths, file);
            if (files != null && files.length > 0) return files[0];
        } else if (size > 1) {
            Path path = Files.createTempDirectory("ansj_dic");
            file = path.toFile();
            createTempFile(paths, file);
            return file;
        }
        return null;
    }

    /**
     * 创建临时文件，程序退出后会自动删除
     *
     * @param paths 文件路径
     * @param file  目录，如果paths.size()==0时file为空
     * @return
     * @throws IOException
     */
    private static File[] createTempFile(List<String> paths, File file) throws IOException {
        File[] libs = new File[paths.size()];
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            String fileName = Paths.get(path).getFileName().toString();
            String extension = null;
            String name = fileName;
            if (fileName.indexOf(".") != -1) {
                extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                name = fileName.substring(0, fileName.length() - (extension).length());
            }
            InputStream stream = UserDefineLibrary.class.getClassLoader().getResourceAsStream(path.substring(1, path.length()));
            File template;
            if (file != null) {
                template = File.createTempFile(name, extension, file);
            } else {
                template = File.createTempFile(name, extension);
            }

            template.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(template)) {
                copy(stream, out);
            }
            libs[i] = template;
        }
        return libs;
    }


    /**
     * 从jar包中读取文件列表
     *
     * @param jarUrl
     * @param callback
     * @throws IOException
     */
    public static void read(URL jarUrl, InputStreamCallback callback) throws IOException {
        if (!"jar".equals(jarUrl.getProtocol())) {
            throw new IllegalArgumentException("Jar protocol is expected but get " + jarUrl.getProtocol());
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null");
        }
        String jarPath = jarUrl.getPath().substring(5);
        String[] paths = jarPath.split("!");
        FileInputStream jarFileInputStream = new FileInputStream(paths[0]);
        readStream(jarFileInputStream, paths[0], 1, paths, callback);
    }

    private static void readStream(InputStream jarFileInputStream, String name, int i, String[] paths, InputStreamCallback callback) throws IOException {
        if (i == paths.length) {
            callback.onFile(name, jarFileInputStream);
            return;
        }
        ZipInputStream jarInputStream = new ZipInputStream(jarFileInputStream);
        try {
            ZipEntry jarEntry;
            while ((jarEntry = jarInputStream.getNextEntry()) != null) {
                String jarEntryName = "/" + jarEntry.getName();
                if (!jarEntry.isDirectory() && jarEntryName.startsWith(paths[i])) {
                    byte[] byteArray = copyStream(jarInputStream, jarEntry);
                    LIBRARYLOG.debug("Entry " + jarEntryName + " with size " + jarEntry.getSize() + " and data size " + byteArray.length);
                    readStream(new ByteArrayInputStream(byteArray), jarEntryName, i + 1, paths, callback);
                }
            }
        } finally {
            jarInputStream.close();
        }
    }

    private static byte[] copyStream(InputStream in, ZipEntry entry)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long size = entry.getSize();
        if (size > -1) {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n = 0;
            long count = 0;
            while (-1 != (n = in.read(buffer)) && count < size) {
                baos.write(buffer, 0, n);
                count += n;
            }
        } else {
            while (true) {
                int b = in.read();
                if (b == -1) {
                    break;
                }
                baos.write(b);
            }
        }
        baos.close();
        return baos.toByteArray();
    }


    private static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    private static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    private static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public interface InputStreamCallback {
        void onFile(String name, InputStream is) throws IOException;
    }

}
