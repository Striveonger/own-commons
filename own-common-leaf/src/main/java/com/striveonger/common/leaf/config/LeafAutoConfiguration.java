package com.striveonger.common.leaf.config;

import com.striveonger.common.core.web.SpringContextHolder;
import com.striveonger.common.leaf.core.FitIDGen;
import com.striveonger.common.leaf.core.IDGen;
import com.striveonger.common.leaf.core.segment.SegmentIDGen;
import com.striveonger.common.leaf.service.AllocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;

/**
 * @author Mr.Lee
 * @since 2024-08-27 22:42
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.leaf.*"})
public class LeafAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(LeafAutoConfiguration.class);

    @Bean
    public IDGen fitIDGen() {
        SegmentIDGen segment = null;
        try {
            AllocService service = SpringContextHolder.getBean(AllocService.class);
            segment = new SegmentIDGen(service);
        } catch (Exception e) {
            log.warn("SegmentIDGen init failed, use default IDGen", e);
        }
        return new FitIDGen(segment);
    }

}
