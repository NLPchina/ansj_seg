package org.ansj.crf;

import lombok.Getter;
import lombok.SneakyThrows;
import org.ansj.app.crf.pojo.Template;
import org.nlpcn.commons.lang.tire.domain.SmartForest;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.google.common.collect.Maps.newHashMapWithExpectedSize;
import static org.ansj.AnsjContext.NEW_LINE;
import static org.ansj.AnsjContext.TAB;

public class Model implements Serializable {

    public final int[][] ft;

    public final int left;

    public final int right;

    public final int tagNum;

    public final Map<String, Integer> statusMap;

    @Getter
    private final double[][] status;

    @Getter
    private final Map<String, double[][]> grad;

    private final SmartForest<double[][]> smartForest;

    Model(
            final int[][] ft, final int left, final int right, final int tagNum, final Map<String, Integer> statusMap,
            final double[][] status, Map<String, double[][]> grad, final SmartForest<double[][]> smartForest
    ) {
        this.ft = ft;
        this.left = left;
        this.right = right;
        this.tagNum = tagNum;
        this.statusMap = statusMap;
        this.status = status;
        this.grad = grad;
        this.smartForest = grad != null && smartForest == null ? buildForest(grad) : smartForest;
    }

    public Model withTagNum(final int tagNum) {
        return new Model(
                this.ft, this.left, this.right, tagNum, this.statusMap,
                this.status, this.grad, this.smartForest
        );
    }

    public Model withStatusMap(final Map<String, Integer> statusMap) {
        return new Model(
                this.ft, this.left, this.right, this.tagNum, statusMap,
                this.status, this.grad, this.smartForest
        );
    }

    public Model whithoutGrad() {
        return new Model(
                this.ft, this.left, this.right, this.tagNum, this.statusMap,
                this.status, null, this.smartForest
        );
    }

    public double[] feature(final int featureIndex, final char... chars) {
        final SmartForest<double[][]> sf = this.smartForest.getBranch(chars);
        return sf != null && sf.getParam() != null ? sf.getParam()[featureIndex] : null;
    }

    /**
     * @param s1 s1
     * @param s2 s2
     * @return tag转移率
     */
    public double tagRate(final int s1, final int s2) {
        return this.status[s1][s2];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder()
                .append("left:").append(this.left).append(TAB)
                .append("rightr:").append(this.right).append(NEW_LINE);
        for (final int[] ints : this.ft) {
            sb.append(Arrays.toString(ints)).append(NEW_LINE);
        }
        return sb.toString();
    }

    private static SmartForest<double[][]> buildForest(final Map<String, double[][]> grad) {
        final SmartForest<double[][]> smartForest = new SmartForest<>(0.8);
        grad.forEach(smartForest::addBranch);
        return smartForest;
    }

    /**
     * for load crf++ model
     */
    @Deprecated
    public Model(final Template template, final double[][] status, final Map<String, double[][]> grad) {
        this(
                template.ft, template.left, template.right, template.tagNum, template.statusMap,
                status, grad, buildForest(grad)
        );
    }

    @Deprecated
    @SneakyThrows
    public static Model loadModel(final InputStream inputStream) {
        try (final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(inputStream)))) {

            final Template template = (Template) ois.readObject();
            final double[][] status = (double[][]) ois.readObject();

            final int gradSize = ois.readInt();// 总共的特征数
            final Map<String, double[][]> grad = newHashMapWithExpectedSize(gradSize);
            for (int fIdx = 0; fIdx < gradSize; fIdx++) {
                final String key = ois.readUTF();
                final double[][] value = new double[template.ft.length][0];
                for (int j = 0; j < value.length; j++) {
                    int b;
                    while ((b = ois.readByte()) != -1) {
                        if (value[j].length == 0) {
                            value[j] = new double[template.tagNum];
                        }
                        value[j][b] = ois.readFloat();
                    }
                }
                grad.put(key, value);
            }

            final Model model = new Model(template, status, grad);
            model.makeSide(template.left, template.right);
            return model;
        }
    }

    /**
     * 将模型写入
     */
    @Deprecated
    @SneakyThrows
    public static void writeModel(final Template template, final Model model, final OutputStream outputStream) {
        try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputStream)))) {
            oos.writeObject(template); // 配置模板
            oos.writeObject(model.status); // 特征转移率

            oos.writeInt(model.grad.size()); // 总共的特征数
            for (final Map.Entry<String, double[][]> entry : model.grad.entrySet()) {
                oos.writeUTF(entry.getKey());
                for (int i = 0; i < template.ft.length; i++) {
                    final double[] doubles = entry.getValue()[i];
                    for (int j = 0; j < doubles.length; j++) {
                        oos.writeByte(j);
                        oos.writeFloat((float) doubles[j]);
                    }
                    oos.writeByte(-1);
                }
            }
            oos.flush();
        }
    }

    /**
     * 根据模板文件解析特征
     *
     * @param left  l
     * @param right r
     * @throws IOException
     */
    @Deprecated
    private void makeSide(final int left, final int right) throws IOException {
        final List<Element> leftList = new ArrayList<>(Math.abs(left));
        for (int i = left; i < 0; i++) {
            leftList.add(new Element((char) ('B' + i)));
        }

        final List<Element> rightList = new ArrayList<>(right);
        for (int i = 1; i < right + 1; i++) {
            rightList.add(new Element((char) ('B' + i)));
        }
    }

//	public enum MODEL_TYPE {
//		CRF, EMM
//	}
//    public int allFeatureCount = 0;
}