package com.striveonger.common.user.config;

import cn.hutool.crypto.digest.MD5;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-02-24 16:20
 */
@Configuration
public class PasswordConfig {

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
