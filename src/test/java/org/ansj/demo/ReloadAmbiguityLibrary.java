package org.ansj.demo;

import org.ansj.library.UserDefineLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

/**
 * 重新加载用户自定义辞典的两种方式
 *
 * @author ansj
 */
public class ReloadAmbiguityLibrary {

    public static void main(String[] args) throws Exception {
        UserDefineLibrary userDefineLibrary = UserDefineLibrary.getInstance();

        // 从文件中reload
        userDefineLibrary = UserDefineLibrary.getInstance(null, loadFormFile());
        // 通过内存中reload
        userDefineLibrary = UserDefineLibrary.getInstance(null, loadFormStr());

        // 歧义辞典增加新词

        Value value = new Value("三个和尚", "三个", "m", "和尚", "n");
        Library.insertWord(userDefineLibrary.getAmbiguityForest(), value);

        // 歧义辞典删除词
        Library.removeWord(userDefineLibrary.getAmbiguityForest(), "三个和尚");
    }

    private static Forest loadFormStr() {
        final Forest forest = new Forest();
        final Value value = new Value("三个和尚", "三个", "m", "和尚", "n");
        Library.insertWord(forest, value);
        return forest;
    }

    private static Forest loadFormFile() throws Exception {
        // make new forest
        return Library.makeForest("new_Library_Path");
    }
}
