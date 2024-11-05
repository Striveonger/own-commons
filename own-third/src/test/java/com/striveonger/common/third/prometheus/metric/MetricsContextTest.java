package com.striveonger.common.third.prometheus.metric;

import com.striveonger.common.core.ThreadHelper;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MetricsContextTest {

    @Test
    public void test() {
        MetricsContext context = new MetricsContext();
        Map<String, String> labels = Map.of("a", "1", "b", "2");
        ThreadHelper.run(() -> {
            for (int i = 0; i < 20; i++) {
                ThreadHelper.sleepSeconds(1);
                Metric metric = new Metric(Metric.Type.COUNTER, "test", i);
                metric.setHelp("测试指标");
                metric.setLabels(labels);
                context.write(metric);
            }
        });

        ThreadHelper.run(() -> {
            while (true) {
                List<Metric> metrics = context.readAll();
                System.out.println(metrics);
                ThreadHelper.sleepSeconds(5);
            }
        });

        ThreadHelper.sleepSeconds(1000);
    }

    @Test
    public void test2() {
        Metric metric = new Metric(Metric.Type.COUNTER, "test", 1.2);
        metric.setHelp("测试指标");
        metric.setLabels(Map.of("a", "1", "b", "2"));
        // long timestamp = System.currentTimeMillis();
        // metric.setTimestamp(timestamp);
        // System.out.println(timestamp/ 1000);
        System.out.println(metric);
    }
}