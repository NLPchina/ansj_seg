package org.ansj.util;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.stream.Collectors.toList;
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
}
