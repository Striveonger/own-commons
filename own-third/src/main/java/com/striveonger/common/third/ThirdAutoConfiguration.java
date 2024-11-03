package com.striveonger.common.third;

import com.striveonger.common.third.prometheus.PrometheusConfig;
import com.striveonger.common.third.prometheus.PrometheusHolds;
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

/**
 * @author Mr.Lee
 * @since 2024-08-27 22:42
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.third.*"})
public class ThirdAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(ThirdAutoConfiguration.class);


    @Bean
    @ConditionalOnProperty(prefix = "own.prometheus", name = "enabled", havingValue = "true")
    public PrometheusConfig prometheusConfig(Environment environment) {
        Binder binder = Binder.get(environment);
        BindResult<PrometheusConfig> result = binder.bind("own.prometheus", PrometheusConfig.class);
        return result.orElse(null);
    }

    @Bean
    @ConditionalOnBean(PrometheusConfig.class)
    public PrometheusHolds prometheusExporter(PrometheusConfig config) {
        return PrometheusHolds.Builder.builder().config(config).build();
    }


}
