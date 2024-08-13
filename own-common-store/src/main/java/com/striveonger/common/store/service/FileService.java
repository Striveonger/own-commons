package com.striveonger.common.store.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.common.store.entity.FileEntity;
import com.striveonger.common.store.mapper.FileMapper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-25 23:39
 */
@Service
public class FileService extends ServiceImpl<FileMapper, FileEntity> implements IService<FileEntity> {

    public FileEntity getByHashCode(String hashcode) {
        QueryWrapper wrapper = QueryWrapper.create().where(FileEntity::getHashcode).eq(hashcode);
        return getOne(wrapper);
    }
}
