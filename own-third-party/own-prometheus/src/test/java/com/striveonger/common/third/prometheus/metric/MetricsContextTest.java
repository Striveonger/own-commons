package com.striveonger.common.third.prometheus.metric;

import com.striveonger.common.core.ThreadKit;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MetricsContextTest {

    @Test
    public void test() {
        MetricsContext context = new MetricsContext();
        Map<String, String> labels = Map.of("a", "1", "b", "2");

        ThreadKit.run(() -> {
            for (int i = 0; i < 20; i++) {
                ThreadKit.sleep(1, TimeUnit.SECONDS);
                Metric metric = new Metric(Metric.Type.COUNTER, "test", i);
                metric.setHelp("测试指标");
                metric.setLabels(labels);
                context.write(metric);
            }
        });

        ThreadKit.run(() -> {
            while (true) {
                List<Metric> metrics = context.readAll();
                System.out.println(metrics);
                ThreadKit.sleep(5, TimeUnit.SECONDS);
            }
        });

        ThreadKit.sleep(1000, TimeUnit.SECONDS);
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