package com.striveonger.common.user.web.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Dict;
import com.mybatisflex.core.query.QueryWrapper;
import com.striveonger.common.core.MarkGenerate;
import com.striveonger.common.user.entity.UserEntity;
import com.striveonger.common.user.service.UserService;
import com.striveonger.common.user.web.vo.UserRegisterVo;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.result.Result;

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
 * @since 2022-11-03 23:16
 */
@Tag(name = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService usersService;

    @Resource
    private PasswordEncoder encoder;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(UserRegisterVo vo) {
        synchronized (vo.toString().intern()) {
            // 1. 检查用户名和邮箱是否已占用
            QueryWrapper wrapper = QueryWrapper.create().where(UserEntity::getUsername).eq(vo.getUsername())
                                                        .or(UserEntity::getEmail).eq(vo.getEmail());
            long count = usersService.count(wrapper);
            if (count > 0) {
                throw new CustomException(ResultStatus.NON_SUPPORT, "用户已存在");
            }

            // 2. 落库
            String id = MarkGenerate.build(vo.getUsername(), vo.getEmail());
            UserEntity user = new UserEntity();
            user.setId(id);
            user.setUsername(vo.getUsername());
            // 3. 密码加密存储
            user.setPassword(encoder.encode(vo.getPassword()));
            user.setEmail(vo.getEmail());
            user.setStatus(0);
            usersService.save(user);
        }
        return Result.success().message("用户注册成功");
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result login(String username, String password) {
        UserEntity hold = usersService.getOne(QueryWrapper.create().where(UserEntity::getUsername).eq(username));
        if (hold == null) {
            return Result.status(ResultStatus.NOT_FOUND).message("用户不存在");
        }
        String encode = encoder.encode(password);
        if (encode.equals(hold.getPassword())) {
            StpUtil.login(hold.getId());
            // 向Redis写入用户信息 TODO: 存入必要信息(JSON串)
            SaTokenInfo token = StpUtil.getTokenInfo();
            return Result.success().message("登录成功").data(Dict.create().set("token", token.getTokenValue()));
        }
        return Result.fail().message("密码错误");
    }

    /**
     * 用户登出
     * @return 登出结果
     */
    @GetMapping("/logout")
    public Result logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            // 删除Redis中的用户信息 TODO: remove key -> UserID
            return Result.success().message("登出成功");
        }
        return Result.fail().message("无效Token");
    }

}