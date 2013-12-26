package org.ansj.app.crf.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.app.crf.pojo.Element;
import org.ansj.app.crf.pojo.Feature;
import org.ansj.app.crf.pojo.TempFeature;

/**
 * 伟大的crf model
 * @author ansj
 *
 */
public class CRFModel extends Model {

    //Perform regularization
    private double regLik = 0;

    // The L2 regularization coefficient and learning rate for SGD
    private double l2_coeff = 1;
    private double rate = 10;

    /**
     * 每次迭代的grad
     */
    private Map<String, Feature> iteartorGrad = new HashMap<String, Feature>();

    /**
     * 每个句子的grad
     */
    Map<String, Feature> lineGrad = null;

    public CRFModel(InputStream templateStream) throws IOException {
        super(templateStream);
        this.myGrad = new HashMap<String, Feature>();
    }

    public CRFModel(String templatePath) throws IOException {
        super(templatePath);
        this.myGrad = new HashMap<String, Feature>();
    }

    private double dot(double[] A, double[] B) {
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i] * B[i];
        }
        return sum;
    }

    private double softMax(double[] A) {
        // find max A
        double max = Double.MIN_VALUE;

        for (double a : A) {
            if (a > max) {
                a = max;
            }
        }

        double sum = 0;

        for (double a : A) {
            sum += Math.exp(a - max);
        }
        return Math.log(sum) + max;

    }

    @Override
    protected void compute() {
        // TODO Auto-generated method stub

    }

    @Override
    public void calculate(String line, String splitStr) {
        // TODO Auto-generated method stub
        List<Element> list = this.parseLine(line, splitStr);
        calcGradient(list);
    }

    private void calcGradient(List<Element> list) {
        // TODO Auto-generated method stub
        lineGrad = new HashMap<String, Feature>();

        int size = list.size() - template.right;

        //Add the features for the numerator
        for (int index = template.left; index < size; index++) {
            status[list.get(index).getTag()][list.get(index + 1).getTag()]++;
            updateFeature(list, index);
        }
        //Calculate the likelihood and normalizing constant
        double norm = calcBack(template.left, list.get(template.left).getTag(), list);
        //        double lik = dot(grad, w) - norm;
    }

    private void updateFeature(List<Element> list, int index) {
        // TODO Auto-generated method stub
        List<TempFeature> tempFeatures = this.getTempFeatures(list, index);

        for (TempFeature tempFeature : tempFeatures) {
            updateFeature(lineGrad, tempFeature.index, tempFeature.sta, tempFeature.name);
        }
    }

    /**
     * 得到此index所有featrue
     * @param list
     * @param index
     * @return
     */
    private List<TempFeature> getTempFeatures(List<Element> list, int index) {
        // TODO Auto-generated method stub
        List<TempFeature> tempFeatures = new ArrayList<TempFeature>(template.ft.length);
        int sta = list.get(index).getTag();
        char[] chars = null;
        for (int i = 0; i < template.ft.length; i++) {
            chars = new char[template.ft[i].length];
            for (int j = 0; j < chars.length; j++) {
                chars[j] = list.get(index + template.ft[i][j]).name;
            }
            tempFeatures.add(new TempFeature(chars, i, sta));
        }
        return tempFeatures;
    }

    private void updateFeature(Map<String, Feature> grad, int fIndex, int sta, String name) {
        // TODO Auto-generated method stub
        Feature feature = grad.get(name);
        if (feature == null) {
            feature = new Feature(template.ft.length);
            grad.put(name, feature);
        }
        feature.update(fIndex, sta, 1);
    }

    private double calcBack(int index, int tag, List<Element> list) {
        if (index >= list.size()) {
            return 0;
        }
        calcBack(index + 1, list.get(index + 1).getTag(), list);
        
        List<TempFeature> tempFeatures = this.getTempFeatures(list, index) ;
        
        
        return 0;
    }
    

    @Override
    public void writeModel(String path) throws FileNotFoundException, IOException {
        // TODO Auto-generated method stub

    }
}
