package com.striveonger.common.core;

import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Mr.Lee
 * @since 2023-06-17 18:28
 */
public class FileHelper {
    private static final Logger log = LoggerFactory.getLogger(FileHelper.class);

    /**
     * 获取文件的URL
     * @param file
     * @return
     */
    public static URL toURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new CustomException(ResultStatus.ACCIDENT, String.format("Illegal [%s] to URL", file));
        }
    }

    /**
     * 判断文件是否是文本文件
     * @param file
     * @return
     */
    public static boolean isTextFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            int byteRead;
            while ((byteRead = fis.read())!= -1) {
                if (byteRead < 0x20 && byteRead!= 0x09 && byteRead!= 0x0A && byteRead!= 0x0D) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("check file type error...", e);
            return false;
        }
    }
}
