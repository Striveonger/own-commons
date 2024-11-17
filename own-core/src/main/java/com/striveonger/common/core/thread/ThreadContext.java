package com.striveonger.common.core.thread;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.striveonger.common.core.thread.ThreadKit.name;

/**
 * 单线程的上下文内容传递
 *
 * @author Mr.Lee
 * @since 2024-11-17 01:43
 */
public class ThreadContext<T> implements AutoCloseable {
    private final Logger log = LoggerFactory.getLogger(ThreadContext.class);

    private final ThreadLocal<T> context = new TransmittableThreadLocal<>();

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

    public static ThreadContext<Object> of() {
        return ContextHolder.instance;
    }

    private static class ContextHolder {
        private static final ThreadContext<Object> instance = new ThreadContext<>();
    }
}
