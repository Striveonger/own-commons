package com.cecbrain.omm.core.log;

import ch.qos.logback.core.PropertyDefinerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Mr.Lee
 * @description: 日志配置文件中获取IP
 * @date 2024-01-03 16:02
 */
public class LogIpPropertyConfig extends PropertyDefinerBase {
    private static final Logger log = LoggerFactory.getLogger(LogIpPropertyConfig.class);

    private static String ip;

    static {
        try {
            // TODO: 如果有多张网卡, 可能会有问题
            ip = InetAddress.getLocalHost().getHostAddress();
            System.setProperty("app.ip", ip);
        } catch (UnknownHostException e) {
            log.error("获取日志IP地址异常", e);
            ip = null;
        }
    }

    @Override
    public String getPropertyValue() {
        return ip;
    }

    public static String getIp() {
        return ip;
    }
}
