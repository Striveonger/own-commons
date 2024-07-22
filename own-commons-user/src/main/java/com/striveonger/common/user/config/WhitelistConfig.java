package com.striveonger.common.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author Mr.Lee
 * @description: 白名单
 * @date 2024-07-21 22:06
 */
@Configuration
@ConfigurationProperties(prefix = "own.auth")
public class WhitelistConfig {
    private final Logger log = LoggerFactory.getLogger(WhitelistConfig.class);

    private Set<String> whitelist;

    public Set<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(Set<String> whitelist) {
        this.whitelist = whitelist;
    }

    public boolean contains(String uri) {
        return whitelist.contains(uri);
    }
}
