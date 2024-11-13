package com.striveonger.common.core;

import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.Lee
 * @since 2023-06-17 18:28
 */
public class FileKit {
    private static final Logger log = LoggerFactory.getLogger(FileKit.class);

    private FileKit() {}

    public static FileKit of() {
        return new FileKit();
    }

    /**
     * 获取文件的URL
     *
     * @param file
     * @return
     */
    public URL toURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new CustomException(ResultStatus.ACCIDENT, String.format("Illegal [%s] to URL", file));
        }
    }

    /**
     * 判断文件是否是文本文件
     *
     * @param file
     * @return
     */
    public boolean isTextFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            int byteRead;
            while ((byteRead = fis.read()) != -1) {
                if (byteRead < 0x20 && byteRead != 0x09 && byteRead != 0x0A && byteRead != 0x0D) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("check file type error...", e);
            return false;
        }
    }

    public boolean delete(String path) {
        return delete(new File(path));
    }

    public boolean delete(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files!= null) {
                for (File f : files) {
                    delete(f);
                }
            }
        }
        return file.delete();
    }

    public boolean write(File file, byte[] bytes) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes, 0, bytes.length);
        } catch (Exception e) {
            log.error("write file error...", e);
            return false;
        }
        return true;
    }

    public List<String> splitFile(String filePath, int size) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new CustomException(ResultStatus.NON_SUPPORT, "File not exists or is not a file");
        }
        List<File> files = splitFile(file, size);
        return files.stream().map(File::toString).toList();
    }

    /**
     * 文件拆分
     *
     * @param file 待拆分文件
     * @param size 每个文件大小(单位是MB)<br/>
     *             拆分文件最大支持 1GB
     * @return
     */
    public List<File> splitFile(File file, int size) {
        if (size > 2047) {
            // 2048 * 1024 * 1024 > Integer.MAX_VALUE
            throw new CustomException(ResultStatus.NON_SUPPORT, "part file size exceeds 1GB");
        }
        List<File> chunks = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024 * 1024];
            int bytesRead, idx = 1, chunkFileSize = size * 1024 * 1024;
            int len = String.valueOf((int) Math.ceil((double) file.length() / chunkFileSize)).length();
            String filename = file.getName() + ".%0" + len + "d" + ".part";

            // 拆分文件
            File chunk = null;
            while ((bytesRead = fis.read(buffer)) != -1) {
                if (chunk == null || chunk.length() >= chunkFileSize) {
                    // 创建新的文件
                    chunk = new File(file.getParent(), String.format(filename, idx));
                    chunks.add(chunk);
                    idx++;
                }
                try (FileOutputStream fos = new FileOutputStream(chunk, true)) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            log.error("split file error...", e);
        }
        return chunks;
    }

    public void mergeFile(List<String> chunks, String path) {
        List<File> files = new ArrayList<>();
        for (String chunk : chunks) {
            files.add(new File(chunk));
        }
        mergeFile(files, new File(path));
    }

    /**
     * 合并文件
     * @param chunks 待合并文件列表
     * @param target 合并后的文件
     */
    public void mergeFile(List<File> chunks, File target) {
        byte[] buffer = new byte[1024 * 1024];
        if (target.exists()) {
            target.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(target, true)) {
            for (File chunk : chunks) {
                try (FileInputStream fis = new FileInputStream(chunk)) {
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer))!= -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
