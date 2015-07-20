package org.ansj.demo;

import org.ansj.library.UserDefineLibrary;
import org.ansj.util.AnsjContext;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;

import static org.ansj.util.AnsjContext.CONTEXT;

/**
 * 重新加载用户自定义辞典的两种方式
 *
 * @author ansj
 */
public class ReloadUserLibrary {

    private static AnsjContext CONTEXT = CONTEXT();

    public static void main(String[] args) {
        // 从文件中reload
        loadFormFile();
        // 通过内存中reload
        loadFormStr();
    }

    private static void loadFormStr() {
        final Forest forest = new Forest();
        Library.insertWord(forest, "中国  nature  1000");

        new UserDefineLibrary(forest, CONTEXT.loadUserAmbiguityDic());// 将新构建的辞典树替换掉旧的。
    }

    private static void loadFormFile() {
        final Forest forest = new Forest(); // make new forest
        //TODO UserDefineLibrary.loadSingleForestFile(forest, new File("new_Library_Path"), CONTEXT().isSkipUserDefine);

        new UserDefineLibrary(forest, CONTEXT.loadUserAmbiguityDic()); // 将新构建的辞典树替换掉舊的。
    }
}
