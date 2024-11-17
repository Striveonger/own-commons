package com.striveonger.common.core.thread;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.RejectPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mr.Lee
 * @since 2024-09-17 12:25
 */
public class ThreadKit {
    private static final Logger log = LoggerFactory.getLogger(ThreadKit.class);

    private final static AtomicInteger num = new AtomicInteger(0);

    public static String name() {
        return Thread.currentThread().getName();
    }

    public static void sleep(long timeout) {
        sleep(timeout, TimeUnit.MILLISECONDS);
    }

    public static void sleep(long timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            log.error("{}, Interrupted...By ThreadKit", Thread.currentThread().getName(), e);
        }
    }

    public static Thread run(Runnable runnable) {
        return ThreadKit.run(runnable, "thread-kit-" + num.incrementAndGet(), true);
    }

    public static Thread run(Runnable runnable, String name, boolean start) {
        Thread thread = new Thread(runnable, name);
        if (start) {
            thread.start();
        }
        return thread;
    }

    public static <T> FutureTask<T> call(Callable<T> callable) {
        FutureTask<T> task = new FutureTask<>(callable);
        Thread thread = new Thread(task, "thread-kit-" + num.incrementAndGet());
        thread.start();
        return task;
    }

    public static void join(Thread thread) {
        if (Objects.nonNull(thread)) {
            boolean retry = true;
            do {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // ignore
                }
            } while (retry);

        }
    }

    public static ThreadPool.Builder pool() {
        return ThreadPool.Builder.of();
    }

    public static ThreadContext<Object> context() {
        return ThreadContext.of();
    }

    /**
     * 用完的Key, 记得删掉呀...
     */
    public static ThreadBuffer buffer() {
        return ThreadBuffer.of();
    }

    public static ThreadBuffer.Queue queue() {
        return queue("THREAD-KIT-DEFAULT-QUEUE-KEY");
    }

    public static ThreadBuffer.Queue queue(String key) {
        synchronized (key.intern()) {
            return new ThreadBuffer.Queue(key);
        }
    }
}
