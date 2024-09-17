package com.striveonger.common.core.constant;

import java.util.regex.Pattern;

/**
 * 基于规则的校验
 * @author Mr.Lee
 * @since 2023-02-28 18:26
 */
public enum RegularVerify {
    /**
     * ip
     */
    IPV4("^(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"),
    /**
     * 端口
     */
    PORT("^([1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$"),
    /**
     * 变量名
     */
    VARIABLE("^[a-zA-Z][a-zA-Z\\d_]*$"),
    ;

    private final Pattern pattern;

    RegularVerify(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public boolean verify(Object o) {
        String str;
        if (o instanceof String s) {
            str = s;
        } else {
            if (o == null) {
                return false;
            }
            str = String.valueOf(o);
        }
        return this.pattern.matcher(str).matches();
    }
}
