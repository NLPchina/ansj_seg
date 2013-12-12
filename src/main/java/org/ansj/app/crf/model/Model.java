package org.ansj.app.crf.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import love.cq.domain.SmartForest;

import org.ansj.app.crf.pojo.Element;
import org.ansj.app.crf.pojo.Feature;
import org.ansj.util.MatrixUtil;
import org.ansj.util.WordAlert;

public abstract class Model {

    public Template template = null;

    // S0 , B1, M2, E3
    public static final int S = 0;
    public static final int B = 1;
    public static final int M = 2;
    public static final int E = 3;
    public static final Element BEGIN = new Element('B', S);
    public static final Element END = new Element('E', S);
    public static final int TAG_NUM = 4;

    public double[][] featureTagCount = null;

    protected double[][] status = new double[TAG_NUM][TAG_NUM];

    public double[] tagPos = new double[4];

    public int[] tagCount = new int[4];

    protected Map<String, Feature> myGrad;

    protected SmartForest<double[][]> smartForest = null;

    public int allFeatureCount = 0;

    private List<Element> left_list = null;

    private List<Element> right_list = null;

    public Model(String templatePath) throws IOException {
        if (templatePath != null) {
            template = Template.parse(templatePath);
            parseTemplate(template);
        }
        featureTagCount = new double[template.ft.length][TAG_NUM];
    }

    public Model(InputStream templateStream) throws IOException {
        // TODO Auto-generated constructor stub
        if (templateStream != null) {
            template = Template.parse(templateStream);
            parseTemplate(template);
        }
        featureTagCount = new double[template.ft.length][TAG_NUM];
    }

    /**
     * 根据模板文件解析特征
     * @param template
     * @throws IOException 
     */
    private void parseTemplate(Template template) throws IOException {
        // TODO Auto-generated method stub

        left_list = new ArrayList<Element>(template.right);
        for (int i = 0; i < template.left; i++) {
            left_list.add(BEGIN);
        }

        right_list = new ArrayList<Element>(template.right);
        for (int i = 0; i < template.right; i++) {
            right_list.add(END);
        }
    }

    protected void updateFeature(List<Element> list, int index) {
        // TODO Auto-generated method stub
        int sta = list.get(index).getTag();
        char[] chars = null;
        for (int i = 0; i < template.ft.length; i++) {
            chars = new char[template.ft[i].length];
            for (int j = 0; j < chars.length; j++) {
                chars[j] = list.get(index + template.ft[i][j]).name;
            }
            updateFeature(i, sta, chars);
        }
    }

    // P(t)*P(w|t)/P(w)
    protected void logNormalizeSTATUS() {
        double[] sta = null;
        for (int i = 0; i < status.length; i++) {
            sta = status[i];
            double sum = MatrixUtil.sum(sta);
            for (int j = 0; j < sta.length; j++) {
                if (sta[j] == 0) {
                    continue;
                }
                sta[j] = sta[j] / sum;
                sta[j] = -sta[j] * Math.log(sta[j]);
            }
        }

        double sum = MatrixUtil.sum(tagCount);
        for (int i = 0; i < tagCount.length; i++) {
            tagPos[i] = tagCount[i] / sum;
        }
    }

    protected void updateFeature(int fIndex, int sta, char... chars) {
        // TODO Auto-generated method stub
        allFeatureCount++;
        featureTagCount[fIndex][sta]++;
        tagCount[sta]++;
        String name = new String(chars);
        Feature feature = myGrad.get(name);
        if (feature == null) {
            feature = new Feature(template.ft.length);
            myGrad.put(name, feature);
        }
        feature.update(fIndex, sta, 1);
    }

    /**
     * 讲模型写入
     * 
     * @param path
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void writeModel(String path) throws FileNotFoundException, IOException {
        // 计算
        compute();
        System.out.println("compute ok now to save model!");
        // 写模型

        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
            new GZIPOutputStream(new FileOutputStream(path))));

        // 特征转移率
        oos.writeObject(status);
        // 总共的特征数
        oos.writeInt(myGrad.size());
        double[] ds = null;
        for (Entry<String, Feature> entry : myGrad.entrySet()) {
            oos.writeUTF(entry.getKey());
            for (int i = 0; i < template.ft.length; i++) {
                ds = entry.getValue().w[i];
                for (int j = 0; j < ds.length; j++) {
                    oos.writeByte(j);
                    oos.writeFloat((float) ds[j]);
                }
                oos.writeByte(-1);
            }
        }

        oos.flush();
        oos.close();
    }

    /**
     * 模型读取
     * 
     * @param path
     * @return 
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Model loadModel(String templatePath, String modelPath) throws Exception {
        return loadModel(new FileInputStream(templatePath), new FileInputStream(modelPath));

    }

    public static Model loadModel(InputStream templateStream, InputStream modelStream)
                                                                                      throws Exception {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(modelStream)));

            Model model = new Model(templateStream) {

                @Override
                public void calculate(String line, String splitStr) {
                    // TODO Auto-generated method stub
                    throw new RuntimeException(
                        "you can not to calculate ,this model only use by cut ");
                }

                @Override
                protected void compute() {
                    // TODO Auto-generated method stub
                    throw new RuntimeException(
                        "you can not to calculate ,this model only use by cut ");
                }

            };

            model.smartForest = new SmartForest<double[][]>(0.8);
            model.status = (double[][]) ois.readObject();
            // 总共的特征数
            double[][] w = null;
            String key = null;
            int b = 0;
            int featureCount = ois.readInt();
            for (int i = 0; i < featureCount; i++) {
                key = ois.readUTF();
                w = new double[model.template.ft.length][0];
                for (int j = 0; j < model.template.ft.length; j++) {
                    while ((b = ois.readByte()) != -1) {
                        if (w[j].length == 0) {
                            w[j] = new double[TAG_NUM];
                        }
                        w[j][b] = ois.readFloat();
                    }
                }
                model.smartForest.add(key, w);
            }

            return model;
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }

    /**
     * make one line to element sequence
     * 
     * @param line
     * @return
     */
    public List<Element> parseLine(String line, String splitStr) {
        // TODO Auto-generated method stub
        String[] split = line.split(splitStr);
        List<Element> list = new ArrayList<Element>(line.length() + template.left + template.right);
        list.addAll(left_list);
        for (String term : split) {
            makeWordToTempFeature(list, term);
        }
        list.addAll(right_list);
        return list;
    }

    private void makeWordToTempFeature(List<Element> list, String word) {
        // TODO Auto-generated method stub

        List<Element> elements = WordAlert.str2Elements(word);
        switch (elements.size()) {
            case 0:
                break;
            case 1:
                list.add(elements.get(0).updateTag(S));
                break;
            case 2:
                list.add(elements.get(0).updateTag(B));
                list.add(elements.get(1).updateTag(E));
            default:
                list.add(elements.get(0).updateTag(B));
                for (int i = 1; i < elements.size() - 1; i++) {
                    list.add(elements.get(i).updateTag(M));
                }
                list.add(elements.get(elements.size() - 1).updateTag(E));
                break;
        }
    }

    /**
     * 训练计算
     * 
     * @param list
     * @param featureMap
     */
    public abstract void calculate(String line, String splitStr);

    /**
     * 更新特征值
     */
    protected abstract void compute();

    public double[] getFeature(int featureIndex, char... chars) {
        // TODO Auto-generated method stub
        SmartForest<double[][]> sf = smartForest;
        sf = sf.getBranch(chars);
        if (sf == null) {
            return null;
        }
        return sf.getParam()[featureIndex];
    }

    /**
     * tag转移率
     * 
     * @param s1
     * @param s2
     * @return
     */
    public double tagRate(int s1, int s2) {
        // TODO Auto-generated method stub
        return status[s1][s2];
    }

}