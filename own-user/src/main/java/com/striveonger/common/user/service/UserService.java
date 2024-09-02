package com.striveonger.common.user.service;


import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.common.user.entity.UserEntity;
import com.striveonger.common.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-10-30
 */
@Service
public class UserService extends ServiceImpl<UserMapper, UserEntity> implements IService<UserEntity> {

}
