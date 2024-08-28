package com.striveonger.common.user.filter;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.IoUtil;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.result.Result;
import com.striveonger.common.core.utils.JacksonUtils;
import com.striveonger.common.user.config.WhitelistConfig;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

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

    private final WhitelistConfig config;

    public AuthFilter(WhitelistConfig config) {
        this.config = Objects.isNull(config) ? WhitelistConfig.create() : config;
        // 配置User默认的白名单
        this.config.addWhite("/user/login");
        this.config.addWhite("/user/register");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        if (config.match(uri)) {
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
