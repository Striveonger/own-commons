package com.striveonger.common.store.web.controller;

import cn.hutool.core.util.StrUtil;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.result.Result;
import com.striveonger.common.core.utils.FileHash;
import com.striveonger.common.store.context.Storer;
import com.striveonger.common.store.entity.FileEntity;
import com.striveonger.common.store.service.FileService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-30 07:17
 */
@Controller("/file/storage")
public class FileStorageController {
    private final Logger log = LoggerFactory.getLogger(FileStorageController.class);

    @Resource
    private Storer storer;

    @Resource
    private FileService fileService;

    /**
     * 上传文件
     *
     * @param files
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public Result upload(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new CustomException(ResultStatus.NOT_FOUND);
        }
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (StrUtil.isBlank(filename)) {
                continue;
            }
            log.info("upload filename: {}", filename);
            try {
                String id = "";
                // ID id = null; int retry = 3;
                // do {
                //     id = idGenRemoteService.acquireDisrupt(FILE_STORAGE.getKey());
                // } while (retry-- > 0 && Status.exception(id));
                // if (Status.exception(id)) {
                //     return Result.fail().message("User ID create failure");
                // }

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
                FileEntity hold = fileService.getByHashCode(hashcode);
                if (Objects.isNull(hold)) {
                    String filepath = storer.write(targetName, bytes);
                    FileEntity entity = new FileEntity(id, filename, filepath, filetype, hashcode);
                    fileService.save(entity);
                }
            } catch (IOException e) {
                log.error("save file error", e);
                throw new CustomException("save file error");
            }
        }
        return Result.success().message("文件上传成功");
    }

}
