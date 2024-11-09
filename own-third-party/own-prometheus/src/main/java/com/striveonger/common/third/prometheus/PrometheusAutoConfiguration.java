package com.striveonger.common.third.prometheus;

import cn.hutool.core.util.StrUtil;
import com.striveonger.common.core.constant.RegularVerify;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @author Mr.Lee
 * @since 2024-08-27 22:42
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.third.prometheus.*"})
public class PrometheusAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(PrometheusAutoConfiguration.class);

    @Bean
    @ConditionalOnProperty(prefix = "own.prometheus", name = "enabled", havingValue = "true")
    public PrometheusConfig prometheusConfig(Environment environment) {
        Binder binder = Binder.get(environment);
        BindResult<PrometheusConfig> result = binder.bind("own.prometheus", PrometheusConfig.class);
        PrometheusConfig config = result.get();
        if (Objects.nonNull(config)
                && StrUtil.isNotBlank(config.getHost())
                && RegularVerify.PORT.verify(config.getPort())
                && RegularVerify.NUMBER.verify(config.getTimeout())) {
            return config;
        }
        throw new CustomException(ResultStatus.ACCIDENT, "Prometheus missing config info");
    }

    @Bean
    @ConditionalOnBean(PrometheusConfig.class)
    public PrometheusHolds prometheusExporter(PrometheusConfig config) {
        return PrometheusHolds.Builder.builder().config(config).build();
    }

}
