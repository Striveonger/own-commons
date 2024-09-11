package com.striveonger.common.user.config;

import cn.hutool.crypto.digest.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Mr.Lee
 * @since 2024-07-20 17:00
 */
@AutoConfiguration
// 可以灵活配置加载条件哦
// @ConditionalOnClass(DataSource.class)
@AutoConfigurationPackage(basePackages = {"com.striveonger.common.user"})
// @Import({PasswordConfig.class})
public class UserAutoConfiguration {
    private final Logger log = LoggerFactory.getLogger(UserAutoConfiguration.class);

    @Bean
    public PasswordEncoder passwordEncoder() {

        String salt = MD5.create().digestHex("Mr.Lee");

        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence password) {
                String raw = MD5.create().digestHex(password.toString());
                return MD5.create().digestHex(raw + salt);
            }
            @Override
            public boolean matches(CharSequence password, String encode) {
                return encode.equals(encode(password));
            }
        };
    }

}
