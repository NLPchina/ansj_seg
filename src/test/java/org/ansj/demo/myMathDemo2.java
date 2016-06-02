package org.ansj.demo;

import java.io.IOException;

/**
 * @param
 * @Title: 动态规划-背包问题
 * @Author: xuming
 * @Description:
 * @date:2016/5/31 9:22
 * @return
 */
public class myMathDemo2 {

    public static void main(String[] args) throws IOException {

        int c[] = {0, 3, 4, 5};
        int v[] = {0, 4, 5, 6};
        int f[][] = new int[4][11];

        for (int i = 1; i < 4; i++)
            for (int j = 1; j < 11; j++) {
                if (c[i] > j)//如果背包的容量，放不下c[i]，则不选c[i]
                    f[i][j] = f[i - 1][j];
                else {
                    f[i][j] = Math.max(f[i - 1][j], f[i - 1][j - c[i]] + v[i]);//转移方程式
                    System.out.println(f[i][j]);
                }
            }
        System.out.println("last answer: " + f[3][10]);
        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 11; n++) {
                System.out.println("the matrix answer f[" + m + "][" + n + "]:" + f[m][n]);
            }
        }


    }


}
