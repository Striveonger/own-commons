package com.striveonger.common.third.kubernetes.config;

import com.striveonger.common.third.kubernetes.KubernetesKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author Mr.Lee
 * @since 2024-11-10 11:12
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.third.kubernetes.*"})
public class KubernetesAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(KubernetesAutoConfiguration.class);

    @Bean
    @ConditionalOnProperty(prefix = "own.kubernetes", name = "enabled", havingValue = "true")
    public KubernetesKit kubernetes(Environment environment) {
        Binder binder = Binder.get(environment);
        BindResult<KubernetesConfig> result = binder.bind("own.kubernetes", KubernetesConfig.class);
        KubernetesConfig config = result.orElse(null);
        return new KubernetesKit(config);
    }
}
