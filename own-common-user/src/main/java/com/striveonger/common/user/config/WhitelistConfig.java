package com.striveonger.common.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import java.util.HashSet;
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

    private final AntPathMatcher matcher = new AntPathMatcher();

    private final Set<String> whites = new HashSet<>();

    public Set<String> getWhites() {
        return whites;
    }

    public void setWhites(Set<String> whites) {
        this.whites.addAll(whites);
    }

    public void addWhite(String path) {
        this.whites.add(path);
    }

    public boolean match(String url) {
        for (String white : whites) {
            if (matcher.match(white, url)) {
                return true;
            }
        }
        return false;
    }

    public static WhitelistConfig create() {
        return new WhitelistConfig();
    }
}
