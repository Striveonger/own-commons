package com.striveonger.common.leaf.core;

/**
 * @author Mr.Lee
 * @since 2024-08-15 22:49
 */
public class ID {

    private final long id;
    private final Status status;

    public ID(long id, Status status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }


    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
