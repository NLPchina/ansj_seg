package org.ansj.app.crf;

import lombok.SneakyThrows;
import org.ansj.app.crf.pojo.Feature;
import org.ansj.app.crf.pojo.Template;
import org.nlpcn.commons.lang.util.IOUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static org.ansj.util.AnsjUtils.parseFile;
import static org.ansj.util.AnsjUtils.toStream;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class CrfppModelParser {

    public static class TempFeature {

        public int id;
        public int featureId;
        public String name;

        public TempFeature(final String input, final int tagNum) {
            final String[] fragments = input.split(" ");

            this.id = parseInt(fragments[0]) / tagNum;
            int splitIndex = fragments[1].indexOf(":");
            this.featureId = parseInt(fragments[1].substring(1, splitIndex));
            this.name = fragments[1].substring(splitIndex + 1).replace("/", "");
        }
    }

    /**
     * 解析crf++ 生成的模型 txt文件
     * 解析crf++生成的可可视txt文件
     */
    @SneakyThrows
    public static Model parseCrfpp(final String path) {
        final BufferedReader reader = IOUtil.getReader(path, IOUtil.UTF8);
        reader.readLine();// version
        reader.readLine();// cost-factor
        final int maxId = parseInt(reader.readLine().split(":")[1].trim());// maxId
        reader.readLine();// xsize
        reader.readLine();// line

        final Map<String, Integer> statusMap = new HashMap<>();
        // read status
        int tagNum = 0;
        String temp;
        while ((temp = reader.readLine()) != null) {
            if (isBlank(temp)) {
                break;
            }
            statusMap.put(temp, tagNum);
            tagNum++;
        }

        // read template
        final StringBuilder templateStringBuilder = new StringBuilder();
        while ((temp = reader.readLine()) != null) {
            if (isBlank(temp)) {
                break;
            }
            templateStringBuilder.append(temp).append("\n");
        }

        final Template template = parseTemplate(templateStringBuilder.toString())
                .withTagNum(tagNum)
                .withStatusMap(statusMap);
        final double[][] status = new double[tagNum][tagNum];

        final int featureNum = template.ft.length;
        // read first B
        final int bIndex = Integer.parseInt(reader.readLine().split(" ")[0]) / tagNum;

        final TempFeature[] tmpFeatures = new TempFeature[maxId / tagNum];
        while ((temp = reader.readLine()) != null) {
            if (isBlank(temp)) {
                break;
            }
            final TempFeature tmpFeature = new TempFeature(temp, tagNum);
            tmpFeatures[tmpFeature.id] = tmpFeature;
        }

        final HashMap<String, Feature> myGrad = new HashMap<>();
        for (int i = 0; i < tmpFeatures.length; i++) { // 填充 myGrad
            if (i == bIndex) { // 读取转移模板
                for (int j = 0; j < tagNum; j++) {
                    for (int j2 = 0; j2 < tagNum; j2++) {
                        status[j][j2] = Double.parseDouble(reader.readLine());
                    }
                }
                i += tagNum - 1;
                continue;
            }
            final TempFeature tmpFeature = tmpFeatures[i];
            final Feature feature = myGrad.get(tmpFeature.name) != null ?
                    myGrad.get(tmpFeature.name) :
                    new Feature(featureNum, tagNum);
            myGrad.put(tmpFeature.name, feature);
            for (int j = 0; j < tagNum; j++) {
                feature.update(tmpFeature.featureId, j, parseDouble(reader.readLine()));
            }
        }

        return new Model(template, status, myGrad);
    }

    /**
     * 解析配置文件
     */
    public static Template parseTemplate(final String string) {
        return parseTemplate(toStream(string));
    }

    public static Template parseTemplate(final InputStream inputStream) {
        final List<String> lines = parseFile(inputStream, line -> line);

        final int[][] ft = new int[lines.size() - 1][0]; // bug?
        for (int l = 0; l < lines.size() - 1; l++) {
            final String line = lines.get(l);

            final String[] fragments = line.split(":");
            final String[] intFragments = fragments[1].split(" ");
            final int index = parseInt(fragments[0].substring(1));

            final int[] ints = new int[intFragments.length];

            for (int i = 0; i < intFragments.length; i++) {
                ints[i] = parseInt(intFragments[i].substring(intFragments[i].indexOf("[") + 1, intFragments[i].indexOf(",")));
            }
            ft[index] = ints;
        }

        // find max and min
        int left = 0;
        int right = 0;
        for (final int[] ints : ft) {
            for (int val : ints) {
                left = left > val ? val : left;
                right = right < val ? val : right;
            }
        }

        return new Template(ft, left, right);
    }
}
