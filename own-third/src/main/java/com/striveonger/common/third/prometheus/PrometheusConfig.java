package com.striveonger.common.third.prometheus;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Prometheus 配置类
 * @author Mr.Lee
 * @since 2024-08-21 13:56
 */
@Configuration
@ConfigurationProperties(prefix = "own.prometheus")
public class PrometheusConfig {

    private String host;

    private String port;

    private int timeout;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
