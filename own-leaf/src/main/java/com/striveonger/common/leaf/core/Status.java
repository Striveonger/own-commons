package com.striveonger.common.leaf.core;

/**
 * @author Mr.Lee
 * @since 2024-08-15 22:50
 */
public enum Status {
    /**
     * 正常的ID
     */
    SUCCESS,

    /**
     * 异常的ID
     */
    UNUSUAL;

    public static boolean success(ID id) {
        return id != null && Status.SUCCESS.equals(id.getStatus());
    }

    public static boolean unusual(ID id) {
        return id!= null && Status.UNUSUAL.equals(id.getStatus());
    }
}
