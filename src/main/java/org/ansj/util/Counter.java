package org.ansj.util;

/**
 * Created by Ansj on 01/11/2017.
 */

import org.ansj.domain.Term;
import org.ansj.exception.LibraryException;
import org.ansj.library.StopLibrary;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.*;
import org.nlpcn.commons.lang.util.CollectionUtil;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.MapCount;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 词频统计
 */
public class Counter {

    private static final Log LOG = LogFactory.getLog(Counter.class);

    private static final int SIZE = 100000;

    /**
     * @param file     需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
     * @param analysis 分词器构造
     * @param size     统计的最大数字。超过数字做搜索 size*20% -》 size
     * @param stop     过滤条件
     * @return 分词后的统计结果
     */
    public static Result count(File file, final Analysis analysis, int size, final StopRecognition stop) {
        if (stop == null) {
            LOG.warn("Maybe you should add a `StopRecognition`");
        }

        LOG.info("start to count[file=" + file + ", analysis=" + analysis.getClass().getName() + ", size=" + size + ", stop=" + stop + "]");

        final AtomicInteger ai = new AtomicInteger(0);
        long t = System.currentTimeMillis();
        final MapCount<String> mc = new MapCount<String>();
        try {
            Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) throws IOException {
                    File f = p.toFile();
                    if (!f.isHidden() && f.exists() && f.canRead()) {
                        String name = f.getName().trim().toLowerCase();
                        if (name.endsWith(".txt") || name.endsWith(".dic")) {
                            // TODO: 文本文件都是UTF-8编码
                            org.ansj.domain.Result result = analysis.parseStr(IOUtil.getContent(f, IOUtil.UTF8));
                            if (stop != null) {
                                result = result.recognition(stop);
                            }
                            for (Term term : result) {
                                mc.add(term.getRealName());
                            }

                            if (LOG.isDebugEnabled()) {
                                LOG.debug("count file: " + f);
                            }

                            ai.incrementAndGet();

                            return FileVisitResult.CONTINUE;
                        }
                    }

                    LOG.warn("ignore file: " + f);

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new LibraryException(e);
        }

        LOG.info("count " + mc.size() + " words, " + "took " + (System.currentTimeMillis() - t) + "ms");

        // 按词频倒序排, 取前size个
        List<Map.Entry<String, Double>> items = CollectionUtil.sortMapByValue(mc.get(), 1);
        Result ret = new Result();
        ret.map = new HashMap<String, Double>(size);
        for (Map.Entry<String, Double> entry : items) {
            if (ret.map.size() == size) {
                break;
            }

            ret.map.put(entry.getKey(), entry.getValue());
        }

        return ret;
    }


    /**
     * @param file     需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
     * @param analysis 分词器构造
     * @param size     统计的最大数字。超过数字做搜索 size*20% -》 size
     * @return 分词后的统计结果
     */
    public static Result count(File file, Analysis analysis, int size) {
        return count(file, analysis, size, null);
    }


    /**
     * @param file     需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
     * @param analysis 分词器构造
     * @return 分词后的统计结果
     */
    public static Result count(File file, Analysis analysis) {
        return count(file, analysis, SIZE, null);
    }

    /**
     * @param file 需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
     * @return 分词后的统计结果
     */
    public static Result count(File file) {
        return count(file, new ToAnalysis(), SIZE, null);
    }

    public static class Result implements Iterable<Map.Entry<String, Double>> {
        public Map<String, Double> map;

        public void writeToFile(String path, String encoding) throws IOException {
            IOUtil.writeMap(map, path, encoding);
        }

        @Override
        public Iterator<Map.Entry<String, Double>> iterator() {
            return map.entrySet().iterator();
        }
    }

    public static void main(String[] args) throws IOException {
        File f = null;
        Analysis analysis = new ToAnalysis();
        int size = SIZE;
        StopRecognition sr = null;
        String[] dim;
        String output = null;
        for (String arg : args) {
            if (arg.startsWith("--") && arg.contains("=")) {
                dim = arg.split("=");
                if (dim.length >= 2) {
                    switch (dim[0]) {
                        case "--path":
                            f = new File(dim[1]);
                            break;
                        case "--analysis":
                            switch (dim[1]) {
                                case "base_ansj":
                                    analysis = new BaseAnalysis();
                                    break;
                                case "index_ansj":
                                    analysis = new IndexAnalysis();
                                    break;
                                case "dic_ansj":
                                    analysis = new DicAnalysis();
                                    break;
                                case "nlp_ansj":
                                    analysis = new NlpAnalysis();
                                    break;
                                default:
                                    LOG.warn("invalid analysis[" + dim[1] + "], then use `ToAnalysis`");
                                    break;
                            }
                            break;
                        case "--size":
                            size = Integer.parseInt(dim[1]);
                            break;
                        case "--stop":
                            sr = StopLibrary.get(dim[1]);
                            break;
                        case "--output":
                            output = dim[1];
                            break;
                        default:
                            LOG.warn("invalid argument[" + dim[0] + "], allowed arguments: --path, --analysis, --size, --stop, --output");
                            break;
                    }
                }
            }
        }

        if (f == null) {
            LOG.error("source path not found! You must add argument[--path]!");
            System.exit(1);
        }

        if (output == null) {
            LOG.warn("output file not found! You should add argument[--output]!");
        }

        Result result = count(f, analysis, size, sr);
        if (output != null) {
            result.writeToFile(output, IOUtil.UTF8);
        }
    }

}
