package org.ansj.app.crf.model;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import org.ansj.app.crf.pojo.Element;
import org.ansj.app.crf.pojo.Feature;
import org.ansj.util.MatrixUtil;

/**
 * 训练bayes模型
 * 
 * @author ansj
 * 
 */
public class EMMModel extends Model {

    private double[][] featureTagCount = null;

    private double[] tagPos = new double[4];

    private int[] tagCount = new int[4];

    private int maxSize = 5000000;

    private int removeSize = 200000;

    public EMMModel(String templatePath) throws IOException {
        super(templatePath);
        this.myGrad = new HashMap<String, Feature>();
        featureTagCount = new double[template.ft.length][TAG_NUM];
    }

    public EMMModel(InputStream templateStream) throws IOException {
        super(templateStream);
        this.myGrad = new HashMap<String, Feature>();
        featureTagCount = new double[template.ft.length][TAG_NUM];
    }

    @Override
    public void calculate(String line, String splitStr) {
        // TODO Auto-generated method stub
        List<Element> list = this.parseLine(line, splitStr);
        int size = list.size() - template.right;

        status[0][list.get(template.left).getTag()]++;

        for (int index = template.left; index < size; index++) {
            status[list.get(index).getTag()][list.get(index + 1).getTag()]++;
            updateFeature(list, index);
        }
        if (maxSize > 0 && myGrad.size() > maxSize) {
            remove(removeSize);
            System.out.println("开始移除特征频");
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
     * 移除低频特征
     * @param removeSize
     */
    private void remove(int removeSize) {
        // TODO Auto-generated method stub
        int beginRemove = 0;
        out: while (true) {
            if (removeSize < 0) {
                break;
            }
            beginRemove++;
            System.out.println("开始移除特征频少于 " + beginRemove + " 个的，当前还需要移除" + removeSize);

            //不限制移除
            Iterator<Entry<String, Feature>> iterator = myGrad.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Feature> next = iterator.next();
                if (next.getValue().value < beginRemove) {
                    iterator.remove();
                    removeSize--;
                }
            }

            if (removeSize <= 0) {
                break out;
            }

        }

    }

    public void compute() {
        // normalize status
        logNormalizeSTATUS();
        // 计算每个特征的
        Iterator<Entry<String, Feature>> iterator = myGrad.entrySet().iterator();
        System.out.println("共有特征" + myGrad.size());
        while (iterator.hasNext()) {
            Feature feature = iterator.next().getValue();
            if (feature.value < 50) {
                // 删除出现频率很低的词
                iterator.remove();
            } else {
                feature.logNormalize();
            }
        }
        System.out.println("移除后还剩" + myGrad.size());
    }

    @Override
    public void writeModel(String path) throws FileNotFoundException, IOException {
        // TODO Auto-generated method stub
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

}
