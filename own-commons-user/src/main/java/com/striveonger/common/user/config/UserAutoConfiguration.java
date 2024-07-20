package com.striveonger.common.user.config;

import com.striveonger.common.user.mapper.UsersMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-20 17:00
 */
@AutoConfiguration
// 可以灵活配置加载条件哦
// @ConditionalOnClass(DataSource.class)
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.user.*"})
@Import({PasswordConfig.class})
public class UserAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(UserAutoConfiguration.class);

    // @Resource
    // private UsersMapper usersMapper;
    //
    // @Bean
    // public UsersMapper demoMapper() {
    //     return usersMapper;
    // }
}
