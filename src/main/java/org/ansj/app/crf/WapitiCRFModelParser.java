package org.ansj.app.crf;

import lombok.SneakyThrows;
import org.ansj.app.crf.pojo.Feature;
import org.ansj.app.crf.pojo.Template;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static org.ansj.app.crf.CrfppModelParser.parseTemplate;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class WapitiCRFModelParser {

    /**
     * 解析wapiti 生成的模型 dump 出的文件
     *
     * @param modelFile    modelFile
     * @param templateFile templateFile
     * @param maxSize      模型存储内容大小
     */
    @SneakyThrows
    public static Model parseWapitiCRFModel(final String modelFile, final String templateFile, final int maxSize) {
        // read config
        final String templateString = IOUtil.getContent(IOUtil.getReader(templateFile, IOUtil.UTF8));

        // 填充
        final List<String> statusLines = new ArrayList<>();
        try (final BufferedReader modelReader = IOUtil.getReader(modelFile, IOUtil.UTF8)) {
            String temp;
            while ((temp = modelReader.readLine()) != null) {
                if (StringUtil.isNotBlank(temp) && temp.charAt(0) == 'b') {
                    statusLines.add(temp);
                }
            }
        }

        int tagNum = 0;
        final Map<String, Integer> statusMap = new HashMap<>();
        for (final String str : statusLines) {
            String[] split = str.split("\t");
            tagNum = addStatus(statusMap, split[1], tagNum);
            tagNum = addStatus(statusMap, split[2], tagNum);
        }

        final double[][] status = new double[tagNum][tagNum];
        for (final String str : statusLines) {
            String[] split = str.split("\t");
            status[statusMap.get(split[1])][statusMap.get(split[2])] = parseDouble(split[3]);
        }

        //fix status range sbme
        status[statusMap.get("S")][statusMap.get("E")] = Double.MIN_VALUE;
        status[statusMap.get("S")][statusMap.get("M")] = Double.MIN_VALUE;

        status[statusMap.get("B")][statusMap.get("B")] = Double.MIN_VALUE;
        status[statusMap.get("B")][statusMap.get("S")] = Double.MIN_VALUE;

        status[statusMap.get("M")][statusMap.get("S")] = Double.MIN_VALUE;
        status[statusMap.get("M")][statusMap.get("B")] = Double.MIN_VALUE;

        status[statusMap.get("E")][statusMap.get("M")] = Double.MIN_VALUE;
        status[statusMap.get("E")][statusMap.get("E")] = Double.MIN_VALUE;

        final Template template = parseTemplate(templateString).withTagNum(tagNum).withStatusMap(statusMap);

        // read feature
        final Map<String, Feature> myGrad = new HashMap<>();
        try (final BufferedReader modelReader = IOUtil.getReader(modelFile, IOUtil.UTF8)) {
            String temp;
            while ((temp = modelReader.readLine()) != null) {
                if (isNotBlank(temp) && temp.charAt(0) == 'u') {
                    parseGrad(statusMap, myGrad, temp, template.ft.length, tagNum);
                }
                // 后面的不保留
                if (maxSize > 0 && myGrad.size() > maxSize) {
                    break;
                }
            }
        }

        return new Model(template, status, myGrad);
    }

    /**
     * @param statusMap statusMap
     * @param stat      stat
     * @param tagNum    tagNum
     * @return tagNum or tagNum + 1
     */
    public static int addStatus(
            final Map<String, Integer> statusMap,
            final String stat,
            final int tagNum
    ) {
        if (statusMap.containsKey(stat)) {
            return tagNum;
        } else {
            statusMap.put(stat, tagNum);
            return tagNum + 1;
        }
    }

    private static void parseGrad(
            final Map<String, Integer> statusMap,
            final Map<String, Feature> myGrad,
            final String temp,
            final int featureNum,
            final int tagNum
    ) {
        final String[] fragments = temp.split("\t");

        final int mIndex = fragments[0].indexOf(":");

        final String name = fixName(fragments[0].substring(mIndex + 1));

        int fIndex = Integer.parseInt(fragments[0].substring(1, mIndex));
        int sta = statusMap.get(fragments[2]);
        double step = Double.parseDouble(fragments[3]);

        Feature feature = myGrad.get(name);
        if (feature == null) {
            feature = new Feature(featureNum, tagNum);
            myGrad.put(name, feature);
        }
        feature.update(fIndex, sta, step);
    }

    private static String fixName(final String substring) {
        final StringBuilder result = new StringBuilder();
        final String[] fragments = substring.split(" ");
        for (final String fragment : fragments) {
            result.append(fragment.startsWith("_x") ?
                            String.valueOf((char) ('B' + parseInt(fragment.substring(2)))) :
                            fragment
            );
        }
        return result.toString();
    }
}
