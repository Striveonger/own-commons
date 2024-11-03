package com.striveonger.common.third.prometheus.metric;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Mr.Lee
 * @since 2024-11-02 21:36
 */
public class Metric {

    /**
     * 指标类型
     */
    private final Type type;

    /**
     * 指标名
     */
    private final String name;

    /**
     * 指标值
     */
    private final String value;

    /**
     * 指标时间
     */
    private Long timestamp;

    /**
     * 指标的标签
     */
    private Map<String, String> labels;

    /**
     * 指标的帮助信息
     */
    private String help;

    public Metric(Type type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public void setTimestamp(Long timestamp) {
        String s = String.valueOf(timestamp);
        int diff = 13 - s.length();
        if (diff == 0) {
            this.timestamp = timestamp;
        } else if (diff > 0) {
            this.timestamp = (long) (timestamp * Math.pow(10, diff));
        } else {
            this.timestamp = (long) (timestamp / Math.pow(10, -diff));
        }
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    @Override
    public String toString() {
        String labelString = Optional.ofNullable(labels).orElse(Map.of()).entrySet().stream().map(e -> e.getKey() + "=\"" + e.getValue() + "\"").collect(Collectors.joining(","));
        String result = "";
        result += String.format("# HELP %s %s\n", name, help);
        result += String.format("# TYPE %s %s\n", name, type);
        if (Objects.nonNull(timestamp)) {
            // 注意: 需要Prometheus开启乱序写入
            // timestamp 如果距离当前时间太远, 会导致数据丢失(Prometheus 采集数据的时间间隔不能太短)
            // timestamp 需要是13位的哦~
            result += String.format("%s{%s} %s %s", name, labelString, value, timestamp);
        } else {
            result += String.format("%s{%s} %s", name, labelString, value);
        }
        result += "\n";
        return result;
    }

    public enum Type {
        /**
         * 计数器
         */
        counter,
        /**
         * 仪表盘
         */
        gauge,
        /**
         * 摘要
         */
        summary,
        /**
         * 直方图
         */
        histogram
    }
}
