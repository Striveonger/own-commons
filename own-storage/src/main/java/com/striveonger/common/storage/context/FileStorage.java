package com.striveonger.common.storage.context;

import cn.hutool.core.io.FileUtil;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.storage.config.StorageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * 文件存储器
 *
 * @author Mr.Lee
 * @since 2024-07-26 01:25
 */
public class FileStorage implements Storage {
    private final Logger log = LoggerFactory.getLogger(FileStorage.class);

    private final String folder;

    public FileStorage(StorageConfig.FileStoreConfig config) {
        this.folder = config.getFolder();
    }

    @Override
    public byte[] read(String path) {
        try {
            return FileUtil.readBytes(url(path));
        } catch (Exception e) {
            throw CustomException.of(ResultStatus.FAIL).message("读取文件失败");
        }
    }

    @Override
    public void write(String path, byte[] bytes) {
        try {
            FileUtil.writeBytes(bytes, url(path));
        } catch (Exception e) {
            throw CustomException.of(ResultStatus.FAIL).message("写入文件失败");
        }
    }

    @Override
    public void delete(String path) {
        try {
            String url = url(path);
            boolean exists = FileUtil.exists(Path.of(url), false);
            if (exists) {
                FileUtil.del(url);
            }
        } catch (Exception e) {
            throw CustomException.of(ResultStatus.FAIL).message("删除文件失败");
        }
    }

    private String url(String path) {
        return folder + path;
    }
}
