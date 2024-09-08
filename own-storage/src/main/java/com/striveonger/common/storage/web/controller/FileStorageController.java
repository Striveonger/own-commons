package com.striveonger.common.storage.web.controller;

import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.result.Result;
import com.striveonger.common.storage.context.Storage;
import com.striveonger.common.storage.service.FileService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import static com.striveonger.common.storage.context.Storage.StorageType.FILE;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-30 07:17
 */
@Controller("/file/storage")
public class FileStorageController {
    private final Logger log = LoggerFactory.getLogger(FileStorageController.class);

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
        fileService.upload(files, FILE);
        return Result.success().message("文件上传成功");
    }

}
