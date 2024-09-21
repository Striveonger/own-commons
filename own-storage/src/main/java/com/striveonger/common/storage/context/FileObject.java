package com.striveonger.common.storage.context;

import com.striveonger.common.storage.constant.FileType;

import java.io.ByteArrayInputStream;

/**
 * @author Mr.Lee
 * @since 2024-09-21 15:28
 */
public class FileObject {

    private final String filename;
    private final String filepath;
    private final FileType type;
    private final byte[] bytes;

    public FileObject(String filename, String filepath, byte[] bytes) {
        this.filename = filename;
        this.filepath = filepath;
        String prefix = filename.substring(filename.lastIndexOf(".") + 1);
        this.type = FileType.of(prefix);
        this.bytes = bytes;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public FileType getType() {
        return type;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public ByteArrayInputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }
}
