package com.striveonger.common.storage.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.utils.FileHash;
import com.striveonger.common.leaf.core.IDGen;
import com.striveonger.common.storage.context.Storage;
import com.striveonger.common.storage.entity.FileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Mr.Lee
 * @since 2024-09-11 21:58
 */
@Service
public class StorageService {

    private final Logger log = LoggerFactory.getLogger(StorageService.class);

    private final IDGen fitIDGen;

    private final FileService fileService;

    public StorageService(IDGen fitIDGen, FileService fileService) {
        this.fitIDGen = fitIDGen;
        this.fileService = fileService;
    }

    @Transactional
    public void upload(MultipartFile[] files, Storage.StorageType type) {
        Storage storage = Storage.Factory.of(type);
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (StrUtil.isBlank(filename)) {
                continue;
            }
            log.info("upload filename: {}", filename);
            try {
                byte[] bytes = file.getBytes();
                String hashcode = FileHash.SHA512.code(bytes);
                log.info("file hashcode: {}", hashcode);
                // 1. check hashcode is exist
                if (StrUtil.isBlank(hashcode)) {
                    throw new CustomException("generate file hashcode error...");
                }
                FileEntity hold = fileService.getByHashCode(hashcode);
                if (Objects.nonNull(hold)) {
                    continue;
                }
                String id = fitIDGen.next().toString();
                // 2. generate file info
                String filetype = StrUtil.subAfter(filename, '.', true);
                String filepath = String.format("%s%s%s%s.%s", File.separator, DateUtil.today(), File.separator, id, filetype);
                storage.write(filepath, bytes);
                FileEntity entity = new FileEntity(id, filename, filepath, filetype, hashcode, type.toString());
                fileService.save(entity);
            } catch (IOException e) {
                log.error("save file error...", e);
                throw new CustomException("save file error...");
            }
        }
    }
}
