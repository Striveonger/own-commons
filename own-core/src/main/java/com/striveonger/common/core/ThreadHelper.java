package com.striveonger.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Lee
 * @since 2024-09-17 12:25
 */
public class ThreadHelper {
    private static final Logger log = LoggerFactory.getLogger(ThreadHelper.class);

    /**
     * 秒级阻塞
     * @param timeout 秒
     */
    public static void sleepSeconds(int timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            log.error("{}, Interrupted...By SleepHelper", Thread.currentThread().getName(), e);
        }
    }

    /**
     * 毫秒级阻塞
     * @param timeout 毫秒
     */
    public static void sleepMilliSeconds(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            log.error("{}, Interrupted...By SleepHelper", Thread.currentThread().getName(), e);
        }
    }


    public static Thread run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public static <T> FutureTask<T> call(Callable<T> callable) {
        FutureTask<T> task = new FutureTask<>(callable);
        Thread thread = new Thread(task);
        thread.start();
        return task;
    }

    public static void join(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error("{}, Interrupted...By ThreadHelper", Thread.currentThread().getName(), e);
        }
    }

}
