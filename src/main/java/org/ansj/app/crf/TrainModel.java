package org.ansj.app.crf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import love.cq.util.IOUtil;

import org.ansj.app.crf.model.CRFModel;
import org.ansj.app.crf.model.EMMModel;
import org.ansj.app.crf.model.Model;
import org.ansj.app.crf.model.Model.MODEL_TYPE;

/**
 * 训练由字构词模型
 * 
 * @author ansj
 * 
 */
public class TrainModel {

    private String splitStr = " ";

    private Model model = null;

    /**
     * 训练model
     * @param splitStr 训练文件词语的分隔符
     * @param templatePath  训练模板的路径
     * @param modelType 模型类型
     * @throws IOException  
     */
    public TrainModel(String splitStr, String templatePath, MODEL_TYPE modelType)
                                                                                 throws IOException {
        init(splitStr,IOUtil.getInputStream(templatePath), modelType);
    }

    public TrainModel(String splitStr, InputStream templateStream, MODEL_TYPE modelType)
                                                                                        throws IOException {
        init(splitStr,templateStream, modelType);
    }

    private void init(String splitStr,InputStream templateStream, MODEL_TYPE modelType) throws IOException {
        // TODO Auto-generated method stub
        this.splitStr = splitStr ;
        switch (modelType) {
            case CRF:
                this.model = new CRFModel(templateStream) ;
                break;
            case EMM:
                this.model = new EMMModel(templateStream);
                break;
        }
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

    
    public void saveModel(String path) throws FileNotFoundException, IOException{
        this.model.writeModel(path) ;
    }
}
