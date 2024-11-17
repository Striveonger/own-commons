package com.striveonger.common.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.striveonger.common.core.thread.ThreadKit.name;

/**
 * 单线程的上下文内容传递
 * @author Mr.Lee
 * @since 2024-11-17 01:43
 */
public class ThreadContext<T> implements AutoCloseable {
    private final Logger log = LoggerFactory.getLogger(ThreadContext.class);

    private final ThreadLocal<T> context = new ThreadLocal<>();

    public void set(T value) {
        context.set(value);
    }

    public T get() {
        return context.get();
    }

    public void clear() {
        close();
    }

    @Override
    public void close() {
        log.info("Thread Context [{}] close...", name());
        context.remove();
    }

    private static ThreadContext<Object> instance;

    public static ThreadContext<Object> of() {
        synchronized (ThreadContext.class) {
            if (instance == null) {
                instance = new ThreadContext<>();
            }
            return instance;
        }
    }
}
