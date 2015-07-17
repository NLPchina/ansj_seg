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
    public static <T> List<T> parseFile(final InputStream inputStream, Function<String, T> mapper) {
        return IOUtils.readLines(inputStream).stream()
                .filter(line -> isNotBlank(line) && !line.trim().startsWith("#"))
                .map(mapper)
                .collect(toList());
    }

    public static BufferedReader getReader(final String name) {
        final InputStream in = getInputStream(name);
        try {
            return new BufferedReader(new InputStreamReader(in, "UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream getInputStream(final String name) {
        // maven工程修改词典加载方式
        return AnsjUtils.class.getResourceAsStream("/" + name);
    }
}
