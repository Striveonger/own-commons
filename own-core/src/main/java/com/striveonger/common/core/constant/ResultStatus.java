package com.striveonger.common.core.constant;

/**
 * 通用状态码 <br/>
 * 如有扩展需求, 继承该枚举类, 在所需扩展类状态码数字 * 100 + 自定义状态码
 * @author Mr.Lee
 * @since 2022-11-08 22:05
 */
public enum ResultStatus {

    /**
     * 默认请求失败标识<br/>
     * 没有准确定义的错误信息, 可以使用
     */
    FAIL(0, "Fail"),

    /**
     * 请求成功标识
     */
    SUCCESS(200, "Success"),

    /**
     * 资源找不到的错误标识
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * 需要用户登录
     */
    NEED_USER_LOGIN(401, "Need User Login"),

    /**
     * 暂时不支持的功能
     */
    NON_SUPPORT(405, "Non Support"),

    /**
     * 系统发生异常的标识
     */
    ACCIDENT(500, "Accident Error"),

    /**
     * 解析内容失败
     */
    ANALYZE_CONTENT_FAIL(510, "Acquire Lock Fail");

    private final int state;
    private final String message;
    private boolean show = false;

    ResultStatus(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public void show() {
        this.show = true;
    }

    public int getState() {
        return state;
    }

    public String getCode() {
        return this.toString();
    }

    public String getMessage() {
        return message;
    }

    public boolean getShow() {
        return show;
    }
}
