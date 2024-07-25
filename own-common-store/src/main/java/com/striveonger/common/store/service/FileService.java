package com.striveonger.common.store.service;

import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.common.store.entity.FileEntity;
import com.striveonger.common.store.mapper.FileMapper;
import org.springframework.stereotype.Service;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-25 23:39
 */
@Service
public class FileService extends ServiceImpl<FileMapper, FileEntity> implements IService<FileEntity> {

}
