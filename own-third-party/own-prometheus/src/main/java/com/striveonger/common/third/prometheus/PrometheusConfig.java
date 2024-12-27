package com.striveonger.common.third.prometheus;

/**
 * Prometheus 配置类
 * @author Mr.Lee
 * @since 2024-08-21 13:56
 */
// @Configuration
// @ConfigurationProperties(prefix = "own.prometheus")
public class PrometheusConfig {

    private Boolean enabled;

    private String host;

    private Integer port;

    /**
     * 超时时间，单位：毫秒
     */
    private Integer timeout;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
