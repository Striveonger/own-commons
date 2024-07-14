package com.striveonger.common.user.service.impl;


import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.common.user.entity.Users;
import com.striveonger.common.user.mapper.UsersMapper;
import com.striveonger.common.user.service.IUsersService;
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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
