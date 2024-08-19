package com.striveonger.common.leaf.service;

import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.common.leaf.entity.AllocEntity;
import com.striveonger.common.leaf.mapper.AllocMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.Lee
 * @since 2024-08-16 00:32
 */
@Service
public class AllocService extends ServiceImpl<AllocMapper, AllocEntity> implements IService<AllocEntity> {
    private final Logger log = LoggerFactory.getLogger(AllocService.class);





}
