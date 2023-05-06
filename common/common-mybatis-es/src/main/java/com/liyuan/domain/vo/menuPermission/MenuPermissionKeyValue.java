package com.liyuan.domain.vo.menuPermission;

import lombok.Data;

import java.util.List;

/**
 * @author liyuan
 * @date 2022/11/10
 * @project exam-cloud
 */
@Data
public class MenuPermissionKeyValue {
    private String title;
    private List<String> permissionId;
}
