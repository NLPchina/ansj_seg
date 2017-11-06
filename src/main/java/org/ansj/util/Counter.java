package org.ansj.util;

/**
 * Created by Ansj on 01/11/2017.
 */

import org.ansj.domain.Term;
import org.ansj.exception.LibraryException;
import org.ansj.library.StopLibrary;
import org.ansj.recognition.Recognition;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.*;
import org.nlpcn.commons.lang.util.CollectionUtil;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 词频统计
 */
public class Counter {

    private static final Log LOG = LogFactory.getLog(Counter.class);

    public static int SIZE = 100000;

    public static float FACTOR = 0.2F;

    /**
     * @param file         需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
     * @param encoding     文件的编码
     * @param analysis     分词器构造
     * @param size         统计的最大数字。超过数字做搜索 size*20% -》 size
     * @param recognitions 过滤条件
     * @return 分词后的统计结果
     */
    public static Result count(File file, String encoding, Analysis analysis, int size, Recognition... recognitions) {
        if (recognitions == null || recognitions.length < 1) {
            LOG.warn("Maybe you should add a `Recognition`");
        }

        if (encoding == null) {
            LOG.warn("encoding is null! Then use UTF-8 encoding!");
            encoding = IOUtil.UTF8;
        }

        LOG.info("start to count[file=" + file + ", encoding=" + encoding + ", analysis=" + analysis.getClass().getName() + ", size=" + size + ", recognitions=" + Arrays.toString(recognitions) + "]");

        AtomicInteger ai = new AtomicInteger(0);
        long t = System.currentTimeMillis();
        Map<String, Integer> hm = new HashMap<String, Integer>();
        if (!file.isHidden() && file.exists() && file.canRead()) {
            try {
                if (file.isDirectory()) {
                    // TODO: 只遍历当前目录下的所有文件
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (File f : files) {
                            if (f.isFile() && !f.isHidden() && f.exists() && f.canRead()) {
                                visitFile(ai, hm, f, encoding, analysis, recognitions);
                            }
                        }
                    }
                } else {
                    visitFile(ai, hm, file, encoding, analysis, recognitions);
                }
            } catch (Exception e) {
                throw new LibraryException(e);
            }
        }

        LOG.info("count " + ai.get() + " files, " + "took " + (System.currentTimeMillis() - t) + "ms");

        // 按词频倒序排, 取前size个
        List<Map.Entry<String, Integer>> items = CollectionUtil.sortMapByValue(hm, 1);
        Result ret = new Result();
        ret.map = new HashMap<String, Integer>(size);
        for (Map.Entry<String, Integer> entry : items) {
            if (ret.map.size() == size) {
                break;
            }

            ret.map.put(entry.getKey(), entry.getValue());
        }

        return ret;
    }

    private static void visitFile(AtomicInteger ai, Map<String, Integer> hm, File f, String encoding, Analysis analysis, Recognition... recognitions) throws IOException {
        String name = f.getName().trim().toLowerCase();
        if (name.endsWith(".txt") || name.endsWith(".dic")) {
            try (BufferedReader br = IOUtil.getReader(f, encoding)) {
                String line;
                org.ansj.domain.Result result;
                while ((line = br.readLine()) != null) {
                    result = analysis.parseStr(line);

                    if (recognitions != null) {
                        for (Recognition r : recognitions) {
                            if (r != null) {
                                result.recognition(r);
                            }
                        }
                    }

                    for (Term term : result) {
                        addTerm(term.getRealName(), hm);
                    }
                }
            }

            ai.incrementAndGet();

            if (LOG.isDebugEnabled()) {
                LOG.debug("count file: " + f);
            }
        }
    }

    private static void addTerm(String term, Map<String, Integer> termMap) {
        if (termMap.containsKey(term)) {
            termMap.put(term, termMap.get(term) + 1);
            return;
        }

        // 超过容量
        int capacity = (int) (SIZE * (1 + FACTOR));
        if (capacity <= termMap.size()) {
            LOG.info("map size over capacity[" + capacity + "], then start to remove low frequency terms");
            List<Map.Entry<String, Integer>> items = CollectionUtil.sortMapByValue(termMap, 1);
            for (Map.Entry<String, Integer> item : items.subList(SIZE, items.size())) {
                termMap.remove(item.getKey());

                if (LOG.isDebugEnabled()) {
                    LOG.debug("term[`" + item.getKey() + "`-" + item.getValue() + "] removed");
                }
            }
        }

        //
        termMap.put(term, 1);
    }


    /**
     * @param file     需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
     * @param analysis 分词器构造
     * @param size     统计的最大数字。超过数字做搜索 size*20% -》 size
     * @return 分词后的统计结果
     */
    public static Result count(File file, Analysis analysis, int size) {
        return count(file, null, analysis, size);
    }

    public static Result count(File file, String encoding, Analysis analysis, int size) {
        return count(file, encoding, analysis, size, (Recognition) null);
    }


    /**
     * @param file     需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
     * @param analysis 分词器构造
     * @return 分词后的统计结果
     */
    public static Result count(File file, Analysis analysis) {
        return count(file, null, analysis);
    }

    public static Result count(File file, String encoding, Analysis analysis) {
        return count(file, encoding, analysis, SIZE, (Recognition) null);
    }

    /**
     * @param file 需要统计词频的文件。如果是文件夹。则此文件夹下递归目录的所有txt
     * @return 分词后的统计结果
     */
    public static Result count(File file) {
        return count(file, (String) null);
    }

    public static Result count(File file, String encoding) {
        return count(file, encoding, new ToAnalysis(), SIZE, (Recognition) null);
    }

    public static class Result implements Iterable<Map.Entry<String, Integer>> {
        public Map<String, Integer> map;

        public void writeToFile(String path, String encoding) throws IOException {
            IOUtil.writeMap(map, path, encoding);
        }

        @Override
        public Iterator<Map.Entry<String, Integer>> iterator() {
            return map.entrySet().iterator();
        }
    }

    public static void main(String[] args) throws IOException {
        File f = null;
        Analysis analysis = new ToAnalysis();
        int size = SIZE;
        StopRecognition sr = null;
        String[] dim;
        String encoding = null, output = null;
        for (String arg : args) {
            if (arg.startsWith("--") && arg.contains("=")) {
                dim = arg.split("=");
                if (dim.length >= 2) {
                    switch (dim[0]) {
                        case "--path":
                            f = new File(dim[1]);
                            break;
                        case "--encoding":
                            encoding = dim[1];
                            break;
                        case "--analysis":
                            switch (dim[1]) {
                                case "base":
                                case "base_ansj":
                                    analysis = new BaseAnalysis();
                                    break;
                                case "index":
                                case "index_ansj":
                                    analysis = new IndexAnalysis();
                                    break;
                                case "dic":
                                case "dic_ansj":
                                    analysis = new DicAnalysis();
                                    break;
                                case "nlp":
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
                            LOG.warn("invalid argument[" + dim[0] + "], allowed arguments: --path, --encoding, --analysis, --size, --stop, --output");
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

        Result result = count(f, encoding, analysis, size, sr);
        if (output != null) {
            result.writeToFile(output, IOUtil.UTF8);
            LOG.info("results have been writen to file[" + output + "] in UTF-8 format");
        }
    }

}
