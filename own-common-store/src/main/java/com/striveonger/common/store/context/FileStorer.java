package com.striveonger.common.store.context;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.web.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author Mr.Lee
 * @description: 文件存储器
 * @date 2024-07-26 01:25
 */
public class FileStorer implements Storer {
    private final Logger log = LoggerFactory.getLogger(FileStorer.class);

    private final String filestorage;

    public FileStorer() {
        filestorage = SpringContextHolder.getProperties("own.file.storage", "");
        if (StrUtil.isBlank(filestorage)) {
            log.warn("未配置文件存储路径");
            throw new CustomException(ResultStatus.NOT_FOUND, "未配置文件存储路径");
        }
    }

    @Override
    public byte[] read(String url) {
        return FileUtil.readBytes(filestorage + url);
    }

    @Override
    public String write(String filename, byte[] bytes) {

        String path = filestorage + filename;
        FileUtil.writeBytes(bytes, path);
        return path;
    }
}
