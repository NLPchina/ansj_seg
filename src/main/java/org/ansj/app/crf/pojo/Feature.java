package org.ansj.app.crf.pojo;

import java.util.Arrays;

import org.ansj.app.crf.model.Model;
import org.ansj.util.MatrixUtil;

public class Feature {

    public double value = 0;

    public double[][] w;

    public Feature(int featureNum) {
        w = new double[featureNum][0];
    }

    public Feature(double[][] w) {
        this.w = w;
    }

    public void update(int fIndex, int sta, int step) {
        value += step;
        if (w[fIndex].length == 0) {
            w[fIndex] = new double[Model.TAG_NUM];
        }
        w[fIndex][sta] += step;
    };

    /**
     * p(w)*P(t|w)/p(t) = p(w|t) p(t|w) = p(t)*p(w|t)/p(w)
     */
    public void logNormalize() {
        // TODO Auto-generated method stub
        // double pw = this.value / model.allFeatureCount;
        //
        // double sum = MatrixUtil.sum(w);
        //
        // for (int i = 0; i < w.length; i++) {
        // for (int j = 0; j < w[i].length; j++) {
        // w[i][j] = pw * (w[i][j] / sum) / ((w[i][j] + 1) /
        // model.featureTagCount[i][j]);
        // if (w[i][j] > 0)
        // w[i][j] = -w[i][j] * Math.log(w[i][j]);
        // }
        // }
        double sum = MatrixUtil.sum(w);
        for (int i = 0; i < w.length; i++) {
            // for (int j = 0; j < w[i].length; j++) {
            // w[i][j] = model.tagPos[j] * (w[i][j] / model.tagCount[j]) /
            // ((w[i][j] + 1) / model.featureTagCount[i][j]);
            // if (w[i][j] > 0)
            // w[i][j] = -w[i][j] * Math.log(w[i][j]);
            // }

            for (int j = 0; j < w[i].length; j++) {
                w[i][j] = w[i][j] / sum;
                if (w[i][j] > 0) {
                    w[i][j] = -w[i][j] * Math.log(w[i][j]);
                }
            }

        }
    }

    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        for (double[] ints : w) {
            sb.append(Arrays.toString(ints));
            sb.append("\n");
        }
        return sb.toString();
    }

}