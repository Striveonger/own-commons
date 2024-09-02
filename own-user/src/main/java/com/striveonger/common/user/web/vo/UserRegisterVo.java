package com.striveonger.common.user.web.vo;

/**
 * @author Mr.Lee
 * @description: 用户注册VO
 * @date 2023-02-20 10:43
 */
public class UserRegisterVo {

    private String username;

    private String password;

    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return username + " & " + email;
    }
}
