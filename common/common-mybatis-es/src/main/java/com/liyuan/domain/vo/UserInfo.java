package com.liyuan.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liyuan.typeHandler.Sex;
import com.liyuan.typeHandler.UserStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author liyuan
 * @date 2022/11/8
 * @project exam-cloud
 */
@Data
public class UserInfo {
    private String id;
    private String username;
    private String password;
    private String name;

    private Sex sex;
    private String phone;
    private String email;
    private String[] map;
    private String headPortrait;
    private String education;

    private UserStatus status;
    private String[] roleName;
    private String[] roleId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date birthday;
    private Date createTime;
    private Date modifiedTime;
}
