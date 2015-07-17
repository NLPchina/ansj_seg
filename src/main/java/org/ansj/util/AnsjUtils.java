package org.ansj.util;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AnsjUtils {

    public static InputStream toStream(final String string) {
        return new ByteArrayInputStream(string.getBytes());
    }

    @SneakyThrows
    public static <T> List<T> parseWithoutBlankOrComment(final InputStream inputStream, final Function<String, T> mapper) {
        return IOUtils.readLines(inputStream)
                .stream()
                .filter(line -> isNotBlank(line) && !line.trim().startsWith("#"))
                .map(mapper)
                .collect(toList());
    }

    public static List<String> rawLinesFromClasspath(final String file) {
        return rawLines(getClasspathResource(file));
    }

    @SneakyThrows
    public static List<String> rawLines(final InputStream inputStream) {
        return IOUtils.readLines(inputStream)
                .stream()
                .collect(toList());
    }

    @SneakyThrows
    public static BufferedReader getClasspathResourceReader(final String name) {
        return new BufferedReader(new InputStreamReader(getClasspathResource(name), "UTF-8"));
    }

    public static InputStream getClasspathResource(final String name) {
        // maven工程修改词典加载方式
        return AnsjUtils.class.getResourceAsStream("/" + name);
    }
}
