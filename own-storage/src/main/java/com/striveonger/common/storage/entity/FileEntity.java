package com.striveonger.common.storage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.striveonger.common.db.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Mr.Lee
 * @since 2024-07-25 23:18
 */
@Schema(name = "文件", description = "文件表")
@Table("t_file")
public class FileEntity extends BaseEntity {

    @Schema(name = "文件ID")
    @Id
    private String id;

    @Schema(name = "文件名")
    private String filename;

    @Schema(name = "文件路径")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String filepath;

    @Schema(name = "文件类型")
    private String filetype;

    @Schema(name = "文件Hash")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hashcode;

    @Schema(name = "文件存储类型")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String storage;

    public FileEntity() { }

    public FileEntity(String id, String filename, String filepath, String filetype, String hashcode, String storage) {
        this.id = id;
        this.filename = filename;
        this.filepath = filepath;
        this.filetype = filetype;
        this.hashcode = hashcode;
        this.storage  = storage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }
}
