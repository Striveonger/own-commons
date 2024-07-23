package com.striveonger.common.user.service.impl;


import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.common.user.entity.User;
import com.striveonger.common.user.mapper.UserMapper;
import com.striveonger.common.user.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-10-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
