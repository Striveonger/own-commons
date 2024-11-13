package com.striveonger.common.storage.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.result.Result;
import com.striveonger.common.core.constant.FileHash;
import com.striveonger.common.core.vo.BasicSearchVo;
import com.striveonger.common.db.utils.PageUtil;
import com.striveonger.common.leaf.core.IDGen;
import com.striveonger.common.storage.context.Storage;
import com.striveonger.common.storage.entity.FileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Objects;

/**
 * @author Mr.Lee
 * @since 2024-09-11 21:58
 */
public class StorageService {

    private final Logger log = LoggerFactory.getLogger(StorageService.class);

    private final IDGen fitIDGen;

    private final FileService fileService;

    public StorageService(IDGen fitIDGen, FileService fileService) {
        this.fitIDGen = fitIDGen;
        this.fileService = fileService;
    }

    @Transactional
    public void upload(String filename, byte[] bytes, Storage.StorageType type) {
        Storage storage = Storage.Factory.of(type);
        if (StrUtil.isBlank(filename)) {
            return;
        }
        log.info("upload filename: {}", filename);
        try {
            String hashcode = FileHash.SHA512.code(bytes);
            log.info("file hashcode: {}", hashcode);
            // 1. check hashcode is exist
            if (StrUtil.isBlank(hashcode)) {
                throw new CustomException("generate file hashcode error...");
            }
            FileEntity hold = fileService.getByHashCode(hashcode);
            if (Objects.nonNull(hold)) {
                return;
            }
            String id = fitIDGen.next().toString();
            // 2. generate file info
            String filetype = StrUtil.subAfter(filename, '.', true);
            String filepath = String.format("%s%s%s%s.%s", File.separator, DateUtil.today(), File.separator, id, filetype);
            storage.write(filepath, bytes);
            FileEntity entity = new FileEntity(id, filename, filepath, filetype, hashcode, type.toString());
            fileService.save(entity);
        } catch (Exception e) {
            log.error("save file error...", e);
            throw new CustomException("save file error...");
        }
    }

    public Result.Page<FileEntity> list(BasicSearchVo vo) {
        Page<FileEntity> page = new Page<>(vo.getFrom(), vo.getSize());
        QueryWrapper wrapper = QueryWrapper.create();
        if (StrUtil.isNotBlank(vo.getSearch())) {
            wrapper.where(FileEntity::getFilename).like(vo.getSearch());
        }
        wrapper.orderBy(FileEntity::getCreateTime, false);
        return PageUtil.convert(fileService.page(page, wrapper));
    }

    public FileEntity get(String id) {
        return fileService.getById(id);
    }

    public byte[] read(String id, Storage.StorageType type) {
        Storage storage = Storage.Factory.of(type);
        FileEntity entity = fileService.getById(id);
        if (Objects.isNull(entity)) {
            throw CustomException.of(ResultStatus.NOT_FOUND).message("未找到文件");
        }
        return storage.read(entity.getFilepath());
    }


}
