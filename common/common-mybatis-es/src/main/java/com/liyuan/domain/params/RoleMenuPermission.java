package com.liyuan.domain.params;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author liyuan
 * @date 2023/3/10
 * @project exam-cloud
 */
@Data
public class RoleMenuPermission {
    private String id;
    private ArrayList<String> addMenuIds;
    private ArrayList<String> deleteMenuIds;
    private ArrayList<String> addPermissionIds;
    private ArrayList<String> deletepermissionIds;
}
