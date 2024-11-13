package com.striveonger.common.leaf.config;

import com.striveonger.common.db.config.MybatisConfiguration;
import com.striveonger.common.leaf.core.FitIDGen;
import com.striveonger.common.leaf.core.segment.SegmentIDGen;
import com.striveonger.common.leaf.core.snowflake.SnowflakeIDGen;
import com.striveonger.common.leaf.service.AllocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @author Mr.Lee
 * @since 2024-08-27 22:42
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.leaf.*"})
public class LeafAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(LeafAutoConfiguration.class);

    @Bean
    // @ConditionalOnProperty(prefix = "own.leaf", name = "enabled", havingValue = "true")
    public LeafConfig leafConfig(Environment environment) {
        Binder binder = Binder.get(environment);
        BindResult<LeafConfig> result = binder.bind("own.leaf", LeafConfig.class);
        return result.orElseGet(LeafConfig::new);
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({MybatisConfiguration.class})
    static class SegmentMode {

        @Bean
        @ConditionalOnBean(DataSource.class)
        public AllocService allocService() {
            return new AllocService();
        }

        @Bean
        @ConditionalOnBean(AllocService.class)
        public FitIDGen fitIDGen(AllocService allocService, LeafConfig config) {
            SegmentIDGen segment = new SegmentIDGen(allocService);
            SnowflakeIDGen snowflake = new SnowflakeIDGen(config.getWorkerId());
            return new FitIDGen(segment, snowflake);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingClass({"com.striveonger.common.db.config.MybatisConfiguration"})
    static class SnowflakeMode {

        @Bean
        @ConditionalOnBean(LeafConfig.class)
        public FitIDGen fitIDGen(LeafConfig config) {
            SnowflakeIDGen snowflake = new SnowflakeIDGen(config.getWorkerId());
            return new FitIDGen(null, snowflake);
        }
    }
}
