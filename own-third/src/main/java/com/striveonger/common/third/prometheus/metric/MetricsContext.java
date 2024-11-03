package com.striveonger.common.third.prometheus.metric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 指标上下文对象
 * @author Mr.Lee
 * @since 2024-11-02 22:07
 */
@Component
public class MetricsContext {
    private final Logger log = LoggerFactory.getLogger(MetricsContext.class);

    private final BlockingDeque<Metric> deque = new LinkedBlockingDeque<>();

    private final Object lock = new Object();

    public void write(Metric metric) {
        // log.info("写入指标：{}", metric);
        deque.offer(metric);
    }

    public List<Metric> readAll() {
        List<Metric> result = new ArrayList<>();
        synchronized (lock) {
            // while (!deque.isEmpty()) {
            //     result.add(deque.poll());
            // }
            deque.drainTo(result);
            return result;
        }
    }
}
