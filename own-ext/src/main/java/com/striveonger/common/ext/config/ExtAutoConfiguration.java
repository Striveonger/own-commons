package com.striveonger.common.ext.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Mr.Lee
 * @since 2024-10-27 13:19
 */
@AutoConfiguration
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.ext.*"})
@EnableAspectJAutoProxy
public class ExtAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(ExtAutoConfiguration.class);

    @Bean
    public SpringBootServletInitializer customSpringBootClassLoader() {
        return new SpringBootServletInitializer() {
            @Override
            protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                log.info("classloader: {}", loader.getName());
                // builder.application().setClassLoader(loader);
                // builder.application().getClassLoader()
                return super.configure(builder);
            }
        };
    }
}
