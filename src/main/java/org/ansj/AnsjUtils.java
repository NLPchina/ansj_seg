package org.ansj;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.ansj.AnsjContext.LIBRARYLOG;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AnsjUtils {

    public static InputStream toStream(final String string) {
        return new ByteArrayInputStream(string.getBytes());
    }

    @SneakyThrows
    public static <T> List<T> linesWithoutBlankAndComments(final InputStream inputStream, final Function<String, T> mapper) {
        return IOUtils.readLines(inputStream)
                .stream()
                .filter(line -> isNotBlank(line) && !line.trim().startsWith("#"))
                .map(mapper)
                .collect(toList());
    }

    @SneakyThrows
    public static List<String> rawLines(final InputStream inputStream) {
        return rawLines(inputStream, UTF_8);
    }

    @SneakyThrows
    public static List<String> rawLines(final InputStream inputStream, final Charset charset) {
        return charset != null ?
                IOUtils.readLines(inputStream, charset).stream().collect(toList()) :
                IOUtils.readLines(inputStream).stream().collect(toList());
    }

    @SneakyThrows
    public static BufferedReader reader(final InputStream resource, final String charsetName) {
        return new BufferedReader(new InputStreamReader(resource, charsetName));
    }

    @SneakyThrows
    public static BufferedReader reader(final InputStream resource) {
        return reader(resource, UTF_8.name());
    }

    @SneakyThrows
    public static InputStream filesystemResource(final String file) {
        return new FileInputStream(file);
    }

    public static InputStream classpathResource(final String name) {
        // maven工程修改词典加载方式
        return AnsjUtils.class.getResourceAsStream("/" + name);
    }

    public static InputStream filesystemDics(final String path) { // 处理词典的路径.或者目录.词典后缀必须为.dic
        if (!isFileAvailable(path)) {
            LIBRARYLOG.warning("init dic warning '" + path + "' file not found or failed to read!");
            return new ByteArrayInputStream(new byte[0]);
        }
        final File file = new File(path);
        final List<FileInputStream> streams = asList(file.isFile() ? new File[]{file} : (file.isDirectory() ? listFiles(file) : new File[0]))
                .stream()
                .filter(f -> f != null && f.getName().trim().endsWith(".dic"))
                .map(AnsjUtils::inputStream)
                .filter(is -> is != null)
                .collect(toList());
        if (streams.size() == 0) {
            LIBRARYLOG.warning("init dic  error '" + path + "' file not found !");
            return new ByteArrayInputStream(new byte[0]);
        }
        return new SequenceInputStream(new Vector<>(streams).elements());
    }

    public static InputStream filesystemDic(final String file) {
        if (!isFileAvailable(file)) {
            LIBRARYLOG.warning("init dic  warning '" + file + "' file not found or failed to read!");
            return new ByteArrayInputStream(new byte[0]);
        }
        return inputStream(new File(file));
    }

    private static boolean isFileAvailable(final String path) {
        return isNotBlank(path) && new File(path).canRead() && !new File(path).isHidden();
    }

    private static File[] listFiles(final File file) {
        final File[] list = file != null ? file.listFiles() : null;
        return list != null ? list : new File[0];
    }

    private static FileInputStream inputStream(final File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
