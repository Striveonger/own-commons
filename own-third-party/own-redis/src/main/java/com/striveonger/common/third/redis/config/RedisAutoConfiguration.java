package com.striveonger.common.third.redis.config;

import com.striveonger.common.core.Jackson;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.third.redis.RedisKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @author Mr.Lee
 * @since 2024-11-10 11:12
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.third.redis.*"})
public class RedisAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(RedisAutoConfiguration.class);

    @Bean
    @ConditionalOnProperty(prefix = "own.redis", name = "enabled", havingValue = "true")
    public RedisKit redis(Environment environment) {
        Binder binder = Binder.get(environment);
        BindResult<RedisConfig> result = binder.bind("own.redis", RedisConfig.class);
        RedisConfig config = result.orElse(null);
        if (Objects.nonNull(config)) {
            log.info("Redis配置: {}", Jackson.toJSONString(config));
            return RedisKit.builder().config(config).build();
        }
        throw CustomException.of(ResultStatus.NOT_FOUND).message("Redis配置为空");

    }
}
