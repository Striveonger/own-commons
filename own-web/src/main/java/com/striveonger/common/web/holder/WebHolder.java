package com.striveonger.common.web.holder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * @author Mr.Lee
 * @since 2024-11-30 11:22
 */
public class WebHolder {
    private final Logger log = LoggerFactory.getLogger(WebHolder.class);

    public static HttpServletRequest getRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    public static HttpServletResponse getResponse() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }

    public static String getAccessIp() {
        return Optional.ofNullable(getRequest()).map(HttpServletRequest::getRemoteAddr).orElse("");
    }

    // TODO: 后续想到什么方法, 再来实现什么吧
}
