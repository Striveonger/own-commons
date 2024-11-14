package com.striveonger.common.storage.web.controller;

import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.result.Result;
import com.striveonger.common.core.vo.BasicVo;
import com.striveonger.common.storage.entity.FileEntity;
import com.striveonger.common.storage.service.StorageService;
import com.striveonger.common.web.ResponseStreamKit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.striveonger.common.storage.context.Storage.StorageType.FILE;

/**
 * @author Mr.Lee
 * @since 2024-07-30 07:17
 */
@Controller
@RequestMapping("/storage")
public class StorageController {
    private final Logger log = LoggerFactory.getLogger(StorageController.class);

    private final StorageService service;

    public StorageController(StorageService service) {
        this.service = service;
    }

    /**
     * 上传文件
     * @param files
     * @return
     */
    @PostMapping("/file/upload")
    @ResponseBody
    public Result upload(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new CustomException(ResultStatus.NOT_FOUND);
        }
        log.info("upload file: {}", files.length);
        for (MultipartFile file : files) {
            try {
                String filename = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                log.info("upload file: {}", filename);
                service.upload(filename, bytes, FILE);
            } catch (IOException e) {
                throw CustomException.of("上传文件失败");
            }
        }
        return Result.success().message("文件上传成功");
    }

    @GetMapping("/file/list")
    @ResponseBody
    public Result list(BasicVo vo) {
        Result.Page<FileEntity> data = service.list(vo);
        return Result.success().page(data);
    }

    @GetMapping("file/preview")
    public void preview(HttpServletRequest request, HttpServletResponse response, String id) {
        FileEntity entity = service.get(id);
        if (Objects.isNull(entity)) {
            throw CustomException.of(ResultStatus.NOT_FOUND).message("未找到文件");
        }
        byte[] bytes = service.read(id, FILE);
        ResponseStreamKit.preview(entity.getFilename(), request, response, bytes);
    }
}
