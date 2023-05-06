package com.liyuan.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liyuan
 * @date 2022/9/9
 * @project exam
 */
@Data
public class UserVo {
    private Long id;
    private String username;
    private String password;
    private Integer status;
    private String clientId;
    private List<String> roles;
}
