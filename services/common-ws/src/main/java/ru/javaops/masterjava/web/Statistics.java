package ru.javaops.masterjava.web;

import com.google.common.collect.EvictingQueue;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Statistics {
    public enum RESULT {
        SUCCESS, FAIL
    }

    private static Statistics instance;

    private static long invocationCount = 0;
    private static long executionTimeAverageMs = 0;
    private static long errorCount = 0;
    private static long successCount = 0;
    private static EvictingQueue<Record> queue;
    static {
        queue= EvictingQueue.create(100);

    }

    public static void count(String payload, long startTime, RESULT result) {
        long now = System.currentTimeMillis();
        int ms = (int) (now - startTime);
        log.info(payload + " " + result.name() + " execution time(ms): " + ms + " average execution time" + getExecutionTimeAverageMs());

        invocationCount++;
        Record record = Record.builder()
                .payload(payload)
                .executionTime(ms)
                .result(result)
                .build();
        queue.add(record);

        if (result == RESULT.SUCCESS) {
            successCount++;
        } else {
            errorCount++;
        }

    }

    public static long getExecutionTimeAverageMs() {
        if (queue.isEmpty()) {
            return 0;
        }
        long executionTimeAverage=0;
        int totalRows = 0;

        for (Record record:queue){
            executionTimeAverage+=record.getExecutionTime();
            totalRows++;
        }
        executionTimeAverage /= totalRows;

        return executionTimeAverage;
    }

    @Getter
    @Setter
    @Builder
    static class Record {
        private String payload;
        private int executionTime;
        private RESULT result;

    }
}
