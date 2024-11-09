package com.striveonger.common.storage.config;

import com.striveonger.common.db.config.MybatisConfiguration;
import com.striveonger.common.leaf.core.FitIDGen;
import com.striveonger.common.leaf.core.IDGen;
import com.striveonger.common.storage.service.FileService;
import com.striveonger.common.storage.service.StorageService;
import com.striveonger.common.storage.web.controller.StorageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;


/**
 * @author Mr.Lee
 * @since 2024-07-20 17:00
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.storage"})
public class StorageAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(StorageAutoConfiguration.class);


    @Bean
    @ConditionalOnProperty(prefix = "own.storage", name = "enabled", havingValue = "true")
    public StorageConfig prometheusConfig(Environment environment) {
        Binder binder = Binder.get(environment);
        BindResult<StorageConfig> result = binder.bind("own.storage", StorageConfig.class);
        return result.get();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({MybatisConfiguration.class, FitIDGen.class})
    static class StorageDataBase {

        @Bean
        @ConditionalOnBean(DataSource.class)
        public FileService fileService() {
            return new FileService();
        }

        @Bean
        @ConditionalOnBean({FileService.class, FitIDGen.class})
        public StorageService storageService(FileService fileService, IDGen fitIDGen) {
            return new StorageService(fitIDGen, fileService);
        }

        @Bean
        @ConditionalOnBean(StorageService.class)
        public StorageController storageController(StorageService storageService) {
            return new StorageController(storageService);
        }
    }

}
