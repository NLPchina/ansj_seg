package org.ansj.demo;

import java.io.IOException;

public class myMathDemo1 {
    public static void main(String[] args) throws IOException {

        int sum = a(1);
        int s = b(1, 1);
        System.out.println("sum:" + sum);
        System.out.println("s:" + s);

    }

    private static int b(int i, int j) {

        if (i == 10) {
            return j;
        }
        return b(i + 1, 2 * j + 2);
    }


    public static int a(int day) {
        if (day == 10) {
            return 1;
        }
        return 2 * a(day + 1) + 2;
    }

}
