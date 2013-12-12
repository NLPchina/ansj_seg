package org.ansj.app.crf.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.ansj.app.crf.SplitWord;
import org.ansj.app.crf.model.BayesModel;
import org.ansj.app.crf.model.Model;

public class RightRate {

    /**
     * 准确率分析
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
            "corpus/rmrb.test")));

        Model model = new BayesModel("template.ftl");
        model.loadModel("bayes.model");
        SplitWord sw = new SplitWord(model);

        String temp_str = null;

        int line_number = 0;// 记录行数
        int ansj_term_number = 0;// 记录ansj中分出的term数量
        int result_num = 0;

        double P = 0.0;
        double R = 0.0;
        double F = 0.0;

        int allError = 0;
        int allSuccess = 0;

        String[] had_words_array = null;// 按split分开后的数组
        String body = null;
        while ((temp_str = br.readLine()) != null) {
            int error = 0;
            int success = 0;
            temp_str = temp_str.trim();
            body = temp_str.replaceAll("\t", "");
            had_words_array = new String[body.length()];
            int offe = 0;

            List<String> paser = sw.cut(body);

            // 填充result
            String[] result = temp_str.split("\t");
            for (int i = 0; i < result.length; i++) {
                try {
                    had_words_array[offe] = result[i];
                    offe += result[i].length();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }

            offe = 0;
            for (String word : paser) {
                if (had_words_array[offe] == null) {
                    error++;
                } else if (had_words_array[offe].equalsIgnoreCase(word)) {
                    success++;
                } else {
                    success++;
                }
                offe += word.length();
            }

            // ansj分出的个数
            ansj_term_number += paser.size();
            // 词语的个数
            result_num += result.length;
            // 累计错误数
            allError += error;
            // 累计正确数
            allSuccess += success;

            if (error > 0) {
                System.out.println("example:" + temp_str);
                System.out.println(" result:"
                                   + paser.toString().replace("[", "").replace("]", "")
                                       .replace(", ", "\t"));
            }
            System.out.println("[" + line_number + "]---准确率P:--"
                               + ((double) success / paser.size()));
            line_number++;
        }
        // 正确数/总词数
        P = allSuccess / (double) ansj_term_number;
        // 正确数/标注文本中的词数
        R = allSuccess / (double) result_num;

        F = (2 * P * R) / (P + R);
        System.out.println("P:" + P);
        System.out.println("R:" + R);
        System.out.println("全文平均准确率--" + F);
        br.close();
    }
}