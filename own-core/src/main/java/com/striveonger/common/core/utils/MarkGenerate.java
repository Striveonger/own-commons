package com.striveonger.common.core.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 根据提供的一组字符串, 生成标记
 * @author Mr.Lee
 * @since  2024-02-20 09:15
 */
public class MarkGenerate {

    public static String build(String ...params) {
        return build(Arrays.stream(params));
    }

    public static String build(Collection<String> coll) {
        return build(coll.stream());
    }

    public static String build(Stream<String> stream) {
        final String s = stream.filter(StrUtil::isNotBlank).collect(Collectors.joining("^_^"));
        return SecureUtil.md5(HexUtil.encodeHexStr(s));
    }
}
