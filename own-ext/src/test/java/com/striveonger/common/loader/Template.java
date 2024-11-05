package com.striveonger.common.loader;

import com.striveonger.common.core.result.Result;

import java.util.Map;

/**
 * @author Mr.Lee
 * @since 2024-11-05 21:30
 */
public interface Template {

    Result list(Map<String, String> map);

}
