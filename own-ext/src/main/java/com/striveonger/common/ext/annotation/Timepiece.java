package com.striveonger.common.ext.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注意: 此注解只作用于被Spring IOC所管理的实例方法
 * @author Mr.Lee
 * @since 2024-10-27 11:34
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Timepiece {


}
