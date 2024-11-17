package com.striveonger.common.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Mr.Lee
 * @since 2024-11-17 11:50
 */
public class ThreadBuffer {
    private static final Logger log = LoggerFactory.getLogger(ThreadBuffer.class);

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    private ThreadBuffer() { }

    public <T> T get(String key) {
        return (T) cache.get(key);
    }


    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public <T> void put(String key, T value) {
        synchronized (key.intern()) {
            cache.put(key, value);
        }
    }

    public void remove(String key) {
        synchronized (key.intern()) {
            cache.remove(key);
        }
    }

    public static ThreadBuffer of() {
        return ThreadBufferHolder.INSTANCE;
    }

    private static class ThreadBufferHolder {
        private static final ThreadBuffer INSTANCE = new ThreadBuffer();
    }

    public static class Queue {

        private final String key;

        public Queue(String key) {
            this.key = key;
            if (!ThreadBuffer.of().containsKey(key)) {
                ThreadBuffer.of().put(key, new LinkedBlockingQueue<>());
            }
        }

        public <T> void offer(T value) {
            BlockingQueue<T> queue = ThreadBuffer.of().get(key);
            if (Objects.nonNull(queue) && Objects.nonNull(value)) {
                queue.offer(value);
            }
        }

        public <T> T take() {
            BlockingQueue<T> queue = ThreadBuffer.of().get(key);
            try {
                return queue.take();
            } catch (Exception e) {
                log.error("take queue error: {}", e.getMessage());
            }
            return null;
        }

        public void close() {
            ThreadBuffer.of().remove(key);
        }


    }

}
