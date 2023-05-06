package com.liyuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto{
    private String id;
    private String username;
    private Integer enabled;
    private String clientId;
}
