package com.striveonger.common.storage.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.common.storage.entity.FileEntity;
import com.striveonger.common.storage.mapper.FileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author Mr.Lee
 * @since 2024-07-25 23:39
 */
public class FileService extends ServiceImpl<FileMapper, FileEntity> implements IService<FileEntity> {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    public FileEntity getByHashCode(String hashcode) {
        QueryWrapper wrapper = QueryWrapper.create().where(FileEntity::getHashcode).eq(hashcode);
        return getOne(wrapper);
    }
}
