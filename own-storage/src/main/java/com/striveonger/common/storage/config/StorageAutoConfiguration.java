package com.striveonger.common.storage.config;

import com.striveonger.common.storage.context.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-20 17:00
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.store.*"})
public class StorageAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(StorageAutoConfiguration.class);

    // private final StorageConfig config;

    public StorageAutoConfiguration(StorageConfig config) {
        // this.config = config;
        Storage.Factory.init(config);
    }

}
