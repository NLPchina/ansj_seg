package org.ansj.app.crf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.ansj.app.crf.model.Model;

/**
 * 训练由字构词模型
 * 
 * @author ansj
 * 
 */
public class TrainModel {

    private String splitStr = " ";

    private Model model = null;

    public TrainModel(String splitStr, boolean isNature, Model model) {
        this.splitStr = splitStr;
        this.model = model;
    }

    int lineNum = 0;

    public void train(String path, String encoding) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }
                lineNum++;
                if (lineNum % 1000 == 0) {
                    System.out.println("train lineNum : " + lineNum);
                }
                model.calculate(line, splitStr);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
