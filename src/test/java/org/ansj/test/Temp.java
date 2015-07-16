package org.ansj.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.nlpcn.commons.lang.util.FileFinder;
import org.nlpcn.commons.lang.util.IOUtil;

public class Temp {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle("library");
        } catch (Exception e) {
            File find = FileFinder.find("library.properties");
            rb = new PropertyResourceBundle(IOUtil.getReader(find.getAbsolutePath(), System.getProperty("file.encoding")));
        }

        if (rb.containsKey("userLibrary"))
            System.out.println(rb.getString("userLibrary"));
        if (rb.containsKey("ambiguityLibrary"))
            System.out.println(rb.getString("ambiguityLibrary"));
        if (rb.containsKey("isSkipUserDefine"))
            System.out.println(Boolean.valueOf(rb.getString("isSkipUserDefine")));
        if (rb.containsKey("isRealName"))
            System.out.println(Boolean.valueOf(rb.getString("isRealName")));
    }
}
