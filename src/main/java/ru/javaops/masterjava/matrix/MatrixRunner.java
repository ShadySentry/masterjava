package ru.javaops.masterjava.matrix;

import java.util.Random;

public class MatrixRunner {

    public static void main(String[] args) {

        int[][] matrixA = MatrixUtil.create(1000);
        int[][] matrixB = MatrixUtil.create(1000);

        System.out.println("base");
        long start = System.currentTimeMillis();
        int[][] resultingMatrix = MatrixUtil.singleThreadMultiplyOpt(matrixA,matrixB);
        double duration = (System.currentTimeMillis() - start) / 1000.;
        out("Single thread time, sec: %.3f", duration);


//        System.out.println("opt");
//        start = System.currentTimeMillis();
//        int[][] resultingMatrix2 = MatrixUtil.singleThreadMultiplyOpt(matrixA,matrixB);
//        duration = (System.currentTimeMillis() - start) / 1000.;
//        out("Single thread time, sec: %.3f", duration);
//
//        System.out.println("opt2");
//        start = System.currentTimeMillis();
//        int[][] resultingMatrix3 = MatrixUtil.singleThreadMultiplyOpt2(matrixA,matrixB);
//        duration = (System.currentTimeMillis() - start) / 1000.;
//        out("Single thread time, sec: %.3f", duration);
//
//        System.out.println("matrices are equals"+equals(resultingMatrix2,resultingMatrix3));
//
//        System.out.println("opt3");
//        start = System.currentTimeMillis();
//        int[][] resultingMatrix4 = MatrixUtil.singleThreadMultiplyOpt3(matrixA,matrixB);
//        duration = (System.currentTimeMillis() - start) / 1000.;
//        out("Single thread time, sec: %.3f", duration);
//
//        System.out.println("matrices are equals "+equals(resultingMatrix3,resultingMatrix4));

    }
    private static boolean equals(int[][] matrixA, int[][] matrixB){
        boolean equals=true;
        for (int i=0;i<1000;i++){
            for (int j=0;j<1000;j++){
                if (matrixA[i][j]!=matrixB[i][j]) {
                    equals=false;
                }
            }
        }
        return equals;
    }

    public static int[][] generateMatrix(){
        int[][] matrix= new int[1000][1000];
        Random random = new Random();

        for(int i=0;i<1000;i++){
            for (int j=0;j<1000;j++){
                matrix[i][j]= random.nextInt();
            }
        }

        return matrix;
    }
    private static void out(String format, double ms) {
        System.out.println(String.format(format, ms));
    }

}
