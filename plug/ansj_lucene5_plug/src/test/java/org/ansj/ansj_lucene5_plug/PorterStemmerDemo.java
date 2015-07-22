package org.ansj.ansj_lucene5_plug;

import lombok.SneakyThrows;
import org.ansj.lucene.util.PorterStemmer;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Test program for demonstrating the Stemmer. It reads a file and stems
 * each word, writing the result to standard out. Usage: Stemmer file-name
 */
public class PorterStemmerDemo {

    @Test
    @SneakyThrows
    public void testPorterStemmer() {
        final String[] args = new String[]{};
        PorterStemmer s = new PorterStemmer();

        for (final String arg : args) {
            try (final InputStream in = new FileInputStream(arg)) {
                byte[] buffer = new byte[1024];
                int bufferLen, offset, ch;

                bufferLen = in.read(buffer);
                offset = 0;
                s.reset();

                while (true) {
                    if (offset < bufferLen)
                        ch = buffer[offset++];
                    else {
                        bufferLen = in.read(buffer);
                        offset = 0;
                        if (bufferLen < 0)
                            ch = -1;
                        else
                            ch = buffer[offset++];
                    }

                    if (Character.isLetter((char) ch)) {
                        s.add(Character.toLowerCase((char) ch));
                    } else {
                        s.stem();
                        System.out.print(s.toString());
                        s.reset();
                        if (ch < 0)
                            break;
                        else {
                            System.out.print((char) ch);
                        }
                    }
                }
            }
        }
    }
}
