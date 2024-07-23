package com.striveonger.common.user.filter;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.IoUtil;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.result.Result;
import com.striveonger.common.core.utils.JacksonUtils;
import com.striveonger.common.core.web.SpringContextHolder;
import com.striveonger.common.user.config.WhitelistConfig;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-21 21:19
 */
@Component
@Order(1)
@WebFilter(filterName = "authFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private final WhitelistConfig whitelist;

    public AuthFilter(WhitelistConfig whitelist) {
        this.whitelist = whitelist == null ? WhitelistConfig.create() : whitelist;
        // 配置User默认的白名单
        this.whitelist.addWhitelist("/user/login");
        this.whitelist.addWhitelist("/user/register");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        if (whitelist.contains(uri)) {
            chain.doFilter(request, response);
        } else {
            if (StpUtil.isLogin()) {
                chain.doFilter(request, response);
            } else {
                log.error("user not login, '{}' need login", uri);
                // throw new CustomException(ResultStatus.NEED_USER_LOGIN);
                res.setContentType(MediaType.APPLICATION_JSON.toString());
                IoUtil.write(res.getOutputStream(), true, JacksonUtils.toJSONBytes(Result.status(ResultStatus.NEED_USER_LOGIN)));
            }
        }
    }
}
