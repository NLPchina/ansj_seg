package org.ansj.app.crf;

public class MatrixUtils {

    public static void dot(double[] left, double[] right) {
        if (right == null) {
            return;
        }
        for (int i = 0; i < right.length; i++) {
            left[i] += right[i];
        }
    }

//    /**
//     * 向量求和
//     */
//    public static double sum(final double[] doubles) {
//        double value = 0;
//        for (final double d : doubles) {
//            value += d;
//        }
//        return value;
//    }
//
//    private static int sum(final int[] ints) {
//        int value = 0;
//        for (final int d : ints) {
//            value += d;
//        }
//        return value;
//    }
//
//    private static double sum(final double[][] doubleArrays) {
//        double value = 0;
//        for (final double[] doubles : doubleArrays) {
//            value += sum(doubles);
//        }
//        return value;
//    }
}
