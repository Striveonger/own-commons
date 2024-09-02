package com.striveonger.common.leaf.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.striveonger.common.db.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Mr.Lee
 * @since 2024-08-16 00:03
 */
@Schema(name = "号段分配对象", description = "号段分配表")
@Table("t_alloc")
public class AllocEntity extends BaseEntity {

    /**
     * 根据tag获取当前分配ID
     */
    @Schema(name = "主键")
    @Id
    private String tag;

    @Schema(name = "当前分配的最大ID")
    private long maxId;

    @Schema(name = "步长")
    private int step;

    @Schema(name = "描述")
    private String description;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
