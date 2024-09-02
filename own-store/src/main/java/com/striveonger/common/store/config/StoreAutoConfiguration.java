package com.striveonger.common.store.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-20 17:00
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.store.*"})
public class StoreAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(StoreAutoConfiguration.class);

}
