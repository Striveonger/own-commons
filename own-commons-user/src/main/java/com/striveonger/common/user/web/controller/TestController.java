package com.striveonger.common.user.web.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.result.Result;
import com.striveonger.common.core.utils.MarkGenerate;
import com.striveonger.common.user.entity.Users;
import com.striveonger.common.user.service.IUsersService;
import com.striveonger.common.user.web.vo.UserRegisterVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-03 23:16
 */
@Tag(name = "Test")
@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    /**
     * hi
     */
    @GetMapping("/hi")
    public Result hello(String name) {
        log.info("Hi, {}~", name);
        return Result.success(String.format("Hi, %s~", name));
    }

}