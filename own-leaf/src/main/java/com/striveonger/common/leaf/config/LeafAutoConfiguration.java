package com.striveonger.common.leaf.config;

import com.striveonger.common.leaf.core.FitIDGen;
import com.striveonger.common.leaf.core.IDGen;
import com.striveonger.common.leaf.core.segment.SegmentIDGen;
import com.striveonger.common.leaf.core.snowflake.SnowflakeIDGen;
import com.striveonger.common.leaf.service.AllocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

/**
 * @author Mr.Lee
 * @since 2024-08-27 22:42
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.leaf.*"})
public class LeafAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(LeafAutoConfiguration.class);

    private final AllocService service;

    private final LeafConfig config;

    public LeafAutoConfiguration(AllocService service, LeafConfig config) {
        this.service = service;
        this.config = Objects.requireNonNullElseGet(config, LeafConfig::new);
    }

    @Bean
    public IDGen fitIDGen() {
        SegmentIDGen segment = null;
        SnowflakeIDGen snowflake = null;
        try {
            // AllocService service = SpringContextHolder.getBean(AllocService.class);
            if (Objects.nonNull(service)) {
                segment = new SegmentIDGen(service);
            }
            if (Objects.nonNull(config)) {
                snowflake = new SnowflakeIDGen(config.getWorkerId());
            }
        } catch (Exception e) {
            log.warn("SegmentIDGen init failed, use default IDGen", e);
        }
        return new FitIDGen(segment, snowflake);
    }

}
