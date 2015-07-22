package org.ansj.demo;

import org.ansj.library.UserLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

import static org.ansj.AnsjContext.CONTEXT;

/**
 * 重新加载用户自定义辞典的两种方式
 *
 * @author ansj
 */
public class ReloadAmbiguityLibrary {

    public static void main(String[] args) throws Exception {
        UserLibrary userLibrary = CONTEXT().getUserLibrary();

        // 从文件中reload
        userLibrary = new UserLibrary(null, loadFormFile());
        // 通过内存中reload
        userLibrary = new UserLibrary(null, loadFormStr());

        // 歧义辞典增加新词

        Value value = new Value("三个和尚", "三个", "m", "和尚", "n");
        Library.insertWord(userLibrary.getAmbiguityForest(), value);

        // 歧义辞典删除词
        Library.removeWord(userLibrary.getAmbiguityForest(), "三个和尚");
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
