package org.ansj.app.crf;

import lombok.SneakyThrows;
import org.ansj.app.crf.pojo.Element;
import org.ansj.app.crf.pojo.Feature;
import org.ansj.app.crf.pojo.Template;
import org.nlpcn.commons.lang.tire.domain.SmartForest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

public class Model {

//	public enum MODEL_TYPE {
//		CRF, EMM
//	}
//    public int allFeatureCount = 0;

    public final Template template;

    private final double[][] status;

    private final Map<String, Feature> myGrad;

    private final SmartForest<double[][]> smartForest;

    /**
     * for parse crf++
     */
    Model(final Template template, double[][] status, final Map<String, Feature> myGrad) {
        this.template = template;
        this.status = status;
        this.myGrad = myGrad;
        this.smartForest = null;
    }

    /**
     * for load model
     */
    protected Model(final Template template, final SmartForest<double[][]> smartForest, final double[][] status) {
        this.template = template;
        this.status = status;
        this.myGrad = null;
        this.smartForest = smartForest;
    }

    public double[] getFeature(final int featureIndex, final char... chars) {
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

    public boolean canWrite() {
        return true;
    }

    /**
     * 将模型写入
     */
    @Deprecated
    @SneakyThrows
    public static void writeModel(final Model model, final OutputStream outputStream) {
        //new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(path)))
        //new ByteArrayOutputStream()

        if (!model.canWrite()) {
            // cant write a loaded Model
            throw new RuntimeException("you can not to calculate ,this model only use by cut");
        }

        try (final ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
            oos.writeObject(model.template); // 配置模板
            oos.writeObject(model.status); // 特征转移率
            oos.writeInt(model.myGrad.size()); // 总共的特征数
            double[] ds;
            for (final Entry<String, Feature> entry : model.myGrad.entrySet()) {
                oos.writeUTF(entry.getKey());
                for (int i = 0; i < model.template.ft.length; i++) {
                    ds = entry.getValue().w[i];
                    for (int j = 0; j < ds.length; j++) {
                        oos.writeByte(j);
                        oos.writeFloat((float) ds[j]);
                    }
                    oos.writeByte(-1);
                }
            }
            oos.flush();
        }
    }

    @SneakyThrows
    public static Model loadModel(final InputStream inputStream) {
        try (final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(inputStream)))) {

            final Template template = (Template) ois.readObject();
            final int tagNum = template.tagNum;
            final int featureNum = template.ft.length;

            final SmartForest<double[][]> smartForest = new SmartForest<>(0.8);
            final double[][] status = (double[][]) ois.readObject();

            // 总共的特征数
            final int featureCount = ois.readInt();
            for (int fIdx = 0; fIdx < featureCount; fIdx++) {
                final String key = ois.readUTF();
                final double[][] w = new double[featureNum][0];
                for (int j = 0; j < featureNum; j++) {
                    int b;
                    while ((b = ois.readByte()) != -1) {
                        if (w[j].length == 0) {
                            w[j] = new double[tagNum];
                        }
                        w[j][b] = ois.readFloat();
                    }
                }
                smartForest.addBranch(key, w);
            }

            final Model model = new Model(template, smartForest, status) {

                @Override
                public boolean canWrite() {
                    return false;
                }
            };
            model.makeSide(template.left, template.right);
            return model;
        }
    }

    /**
     * 根据模板文件解析特征
     *
     * @param left  l
     * @param right r
     * @throws IOException
     */
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
}