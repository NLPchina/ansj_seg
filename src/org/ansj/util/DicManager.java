package org.ansj.util;

import static org.ansj.library.UserDefineLibrary.DEFAULT_FREQ;
import static org.ansj.library.UserDefineLibrary.DEFAULT_NATURE;
import static org.ansj.library.UserDefineLibrary.getUserForestMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import love.cq.domain.Forest;
import love.cq.domain.Value;
import love.cq.library.Library;
import love.cq.util.IOUtil;
import love.cq.util.StringUtil;

/**
 * 用来管理用户自定义词典,和纠正词典
 * @author ansj
 *
 */
public class DicManager {

    private static String userLibraryPath = null;

    private static File[] files = new File[0];

    private Forest forest = null;

    private String name;

    public DicManager(String name) {
        this.name = name;
    }

    static {
        init();
    }

    private static void init() {

        userLibraryPath = MyStaticValue.userLibraryPath;

        if (StringUtil.isNotBlank(userLibraryPath)) {
            return;
        }

        files = new File(userLibraryPath).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                // TODO Auto-generated method stub
                if (name.endsWith(".dic")) {
                    return true;
                }
                return false;
            }
        });
    }

    public void insertFileToLibrary(String filePath, String charEncoding) {

    }

    /**
     * 把一个文件插入到词典中
     * @param file
     * @param charEncoding
     */
    public void insertFileToLibrary(File file, String charEncoding) {
        String forestName = file.getName().split("-")[0];
        Forest forest = getUserForestMap().get(forestName);

        if (forest == null) {
            forest = new Forest();
            getUserForestMap().put(forestName, forest);
        }
        BufferedReader reader = null;
        String temp = null;
        try {
            reader = IOUtil.getReader(new FileInputStream(file), IOUtil.UTF8);
            while (StringUtil.isNotBlank((temp = reader.readLine()))) {
                Library.insertWord(forest, temp.toLowerCase());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.err.println("init library error " + file.getName() + " the error is "
                               + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertWordToLibrary(String word) {
        insertWordToLibrary(word, DEFAULT_NATURE, DEFAULT_FREQ);
    }

    public void insertWordToLibrary(String word, String nature, int freq) {
        Library.insertWord(forest, new Value(word.toLowerCase(), nature, String.valueOf(freq)));
    }

    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * 根据名称得到词典
     * @param name
     * @return
     */
    public static Forest getLibrary(String name) {
        Forest forest = getUserForestMap().get(name);
        if (forest != null) {
            return forest;
        }

        if (files == null || files.length == 0) {
            System.err.println("not find any library by name " + name);
            return null;
        }

        new DicManager(name).initLibraryByName(name);

        return getUserForestMap().get(name);

    }

    /**
     * 线程安全的词典加载
     * @param name
     */
    private void initLibraryByName(String name) {
        Forest forest = null;

        LOCK.lock();

        forest = getUserForestMap().get(name);
        if (forest != null) {
            return;
        }

        DicManager dicManager = new DicManager(name);

        boolean flag = false;

        for (File file : files) {
            if (file.getName().equals(name) || file.getName().startsWith(name + "-")) {
                flag = true;
                dicManager.insertFileToLibrary(file, IOUtil.UTF8);
            }
        }

        if (!flag) {
            System.err.println("not find any library by name " + name);
        }

        LOCK.unlock();
    }
}
