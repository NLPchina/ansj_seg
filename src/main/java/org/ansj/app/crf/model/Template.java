package org.ansj.app.crf.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Template implements Serializable {

    private static final long serialVersionUID = 8265350854930361325L;

    public int left = 2;

    public int right = 2;

    public int[][] ft = { { -2 }, { -1 }, { 0 }, { 1 }, { 2 }, { -2, -1 }, { -1, 0 }, { 0, 1 },
                         { 1, 2 }, { -1, 1 } };

    /**
     * 解析配置文件
     * @param templatePath
     * @return
     * @throws IOException
     */
    public static Template parse(String templatePath) throws IOException {
        // TODO Auto-generated method stub
        return parse(new FileInputStream(templatePath));
    }

    public static Template parse(InputStream templateStream) throws IOException {
        // TODO Auto-generated method stub
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(templateStream));
            return parse(br);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            br.close();
        }
        return null;
    }

    private static Template parse(BufferedReader br) throws IOException {
        Template t = new Template();

        String temp = null;

        List<String> lists = new ArrayList<String>();
        while ((temp = br.readLine()) != null) {
            if (temp.trim().length() == 0 || temp.startsWith("#")) {
                continue;
            }
            lists.add(temp);
        }
        br.close();

        t.ft = new int[lists.size()][0];
        for (int i = 0; i < lists.size(); i++) {
            temp = lists.get(i);
            String[] split = temp.split(",");
            int[] ints = new int[split.length];

            for (int j = 0; j < ints.length; j++) {
                ints[j] = Integer.parseInt(split[j].trim());
            }
            t.ft[i] = ints;
        }

        t.left = 0;
        t.right = 0;
        //find max and min
        for (int[] ints : t.ft) {
            for (int j : ints) {
                t.left = t.left > j ? j : t.left;
                t.right = t.right < j ? j : t.right;
            }
        }
        t.left = Math.abs(t.left);

        return t;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("left:" + left);
        sb.append("\t");
        sb.append("rightr:" + right);
        sb.append("\n");
        for (int[] ints : ft) {
            sb.append(Arrays.toString(ints));
            sb.append("\n");
        }
        return sb.toString();
    }

}
