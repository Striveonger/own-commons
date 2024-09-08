package com.striveonger.common.storage.service;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.utils.FileHash;
import com.striveonger.common.leaf.core.IDGen;
import com.striveonger.common.storage.context.Storage;
import com.striveonger.common.storage.entity.FileEntity;
import com.striveonger.common.storage.mapper.FileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-25 23:39
 */
@Service
public class FileService extends ServiceImpl<FileMapper, FileEntity> implements IService<FileEntity> {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    private final IDGen fitIDGen;

    public FileService(IDGen fitIDGen) {
        this.fitIDGen = fitIDGen;
    }

    public FileEntity getByHashCode(String hashcode) {
        QueryWrapper wrapper = QueryWrapper.create().where(FileEntity::getHashcode).eq(hashcode);
        return getOne(wrapper);
    }

    public void upload(MultipartFile[] files, Storage.StorageType type) {

        Storage storage = Storage.Factory.of(type);
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (StrUtil.isBlank(filename)) {
                continue;
            }
            log.info("upload filename: {}", filename);
            try {
                String id = fitIDGen.next().toString();

                // 1. generate file info
                String filetype = StrUtil.subAfter(filename, '.', true);
                String targetName = String.format("%s.%s", id, filetype);
                byte[] bytes = file.getBytes();
                String hashcode = FileHash.SHA512.code(bytes);
                log.info("file hashcode: {}", hashcode);
                // 2. check hashcode is exist
                if (StrUtil.isBlank(hashcode)) {
                    throw new CustomException("generate file hashcode error...");
                }
                FileEntity hold = this.getByHashCode(hashcode);
                if (Objects.isNull(hold)) {
                    String filepath = storage.write(targetName, bytes);
                    FileEntity entity = new FileEntity(id, filename, filepath, filetype, hashcode);
                    this.save(entity);
                }
            } catch (IOException e) {
                log.error("save file error", e);
                throw new CustomException("save file error");
            }
        }
    }
}
