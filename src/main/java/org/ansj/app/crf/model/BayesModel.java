//package org.ansj.app.crf.model;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map.Entry;
//
//import org.ansj.app.crf.pojo.Element;
//import org.ansj.app.crf.pojo.Feature;
//
///**
// * 训练bayes模型
// * 
// * @author ansj
// * 
// */
//public class BayesModel extends Model {
//
//    public int maxSize = 5000000;
//    public int removeSize = 200000;
//
//    public BayesModel(String template) throws IOException {
//        super(template);
//        this.myGrad = new HashMap<String, Feature>();
//    }
//    
//    public BayesModel(InputStream templateStream) throws IOException {
//        super(templateStream);
//        this.myGrad = new HashMap<String, Feature>();
//    }
//
//
//    @Override
//    public void calculate(String line, String splitStr) {
//        // TODO Auto-generated method stub
//        List<Element> list = this.parseLine(line, splitStr);
//        int size = list.size() - template.right;
//
//        status[0][list.get(template.left).getTag()]++;
//
//        for (int index = template.left; index < size; index++) {
//            status[list.get(index).getTag()][list.get(index + 1).getTag()]++;
//            updateFeature(list, index);
//        }
//        if (maxSize > 0 && myGrad.size() > maxSize) {
//            remove(removeSize);
//            System.out.println("开始移除特征频");
//        }
//    }
//
//    /**
//     * 移除低频特征
//     * @param removeSize
//     */
//    private void remove(int removeSize) {
//        // TODO Auto-generated method stub
//        int beginRemove = 0;
//        out: while (true) {
//            if (removeSize < 0) {
//                break;
//            }
//            beginRemove++;
//            System.out.println("开始移除特征频少于 " + beginRemove + " 个的，当前还需要移除" + removeSize);
//
//            //不限制移除
//            Iterator<Entry<String, Feature>> iterator = myGrad.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Entry<String, Feature> next = iterator.next();
//                if (next.getValue().value < beginRemove) {
//                    iterator.remove();
//                    removeSize--;
//                }
//            }
//
//            if (removeSize <= 0) {
//                break out;
//            }
//
//        }
//
//    }
//
//    public void compute() {
//        // normalize status
//        logNormalizeSTATUS();
//        // 计算每个特征的
//        Iterator<Entry<String, Feature>> iterator = myGrad.entrySet().iterator();
//        System.out.println("共有特征"+myGrad.size());
//        while (iterator.hasNext()) {
//            Feature feature = iterator.next().getValue();
//            if (feature.value < 50) {
//                // 删除出现频率很低的词
//                iterator.remove();
//            } else {
//                feature.logNormalize();
//            }
//        }
//        System.out.println("移除后还剩"+myGrad.size());
//    }
//
//}
