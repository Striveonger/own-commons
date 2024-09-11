package com.striveonger.common.storage.config;

import com.striveonger.common.storage.context.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;


/**
 * @author Mr.Lee
 * @since 2024-07-20 17:00
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.storage"})
public class StorageAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(StorageAutoConfiguration.class);

    public StorageAutoConfiguration(StorageConfig config) {
        Storage.Factory.init(config);
    }
}
