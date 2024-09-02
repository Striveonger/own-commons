package com.striveonger.common.leaf.core;

/**
 * ID生成器
 * @author Mr.Lee
 * @since 2024-08-15 23:02
 */
public interface IDGen {

    ID next();

    ID next(String tag);

}
