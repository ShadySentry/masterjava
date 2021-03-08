package ru.javaops.masterjava.matrix;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final int parts = 4;
        final int portion = matrixSize / parts;
        AtomicInteger processedCount = new AtomicInteger(0);
        for (int step = 0; step < parts; step++) {
            final int unprocessedItems = matrixSize - processedCount.get();
            int currentPortion = Math.min(unprocessedItems, portion);

            final int[] columnC = new int[currentPortion];
            for (int j = 0; j < matrixSize; j++) {
                for (int k = 0; k < matrixSize; k++) {
                    columnC[k] = matrixB[k][j];
                }
                int finalJ = j;
                Future result = executor.submit(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        for (int i = 0; i < matrixSize; i++) {
                                                            int[] thisRow = matrixA[i];
                                                            int sum = 0;
                                                            for (int k = 0; k < matrixSize; k++) {
                                                                sum += thisRow[k] * columnC[k];
                                                            }
                                                            matrixC[i][finalJ] = sum;
                                                        }
                                                    }
                                                }
                );
//                for (int i = 0; i < matrixSize; i++) {
//                    int[] thisRow = matrixA[i];
//                    int sum = 0;
//                    for (int k = 0; k < matrixSize; k++) {
//                        sum += thisRow[k] * columnC[k];
//                    }
//                    matrixC[i][j] = sum;
//                }
            }

        }
        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[] columnC = new int[matrixSize];

        for (int j = 0; j < matrixSize; j++) {
            for (int k = 0; k < matrixSize; k++) {
                columnC[k] = matrixB[k][j];
            }

            for (int i = 0; i < matrixSize; i++) {
                int[] thisRow = matrixA[i];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += thisRow[k] * columnC[k];
                }
                matrixC[i][j] = sum;

            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
