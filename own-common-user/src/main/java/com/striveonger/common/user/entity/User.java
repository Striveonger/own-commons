package com.striveonger.common.user.entity;

import com.mybatisflex.annotation.Table;
import com.striveonger.common.mybatis.entity.BaseEntity;
import com.striveonger.common.core.utils.JacksonUtils;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-10-30
 */
@Schema(name = "Users对象", description = "用户表")
@Table("users")
public class User extends BaseEntity {

    @Schema(name = "用户ID")
    private String id;

    @Schema(name = "用户账号")
    private String username;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "用户邮箱")
    private String email;

    @Schema(name = "手机号码")
    private String tel;

    @Schema(name = "用户性别（1男 2女 0未知）")
    private Boolean sex;

    @Schema(name = "头像地址")
    private String avatar;

    @Schema(name = "帐号状态（0正常 1停用）")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
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
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJSONString(this);
    }

}
