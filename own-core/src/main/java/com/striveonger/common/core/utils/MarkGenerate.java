package com.striveonger.common.core.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Mr.Lee
 * @description: 根据提供字符串数组, 生成标记
 * @date 2024-02-20 09:15
 */
public class MarkGenerate {
    public static String build(String ...params) {
        final String s = Arrays.stream(params).filter(StrUtil::isNotBlank).collect(Collectors.joining("^_^"));
        return SecureUtil.md5(HexUtil.encodeHexStr(s));
    }
}
