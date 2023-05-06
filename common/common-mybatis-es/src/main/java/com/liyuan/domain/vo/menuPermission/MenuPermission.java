package com.liyuan.domain.vo.menuPermission;

import com.liyuan.domain.vo.menuPermission.permissionDic.PermissionDic;
import lombok.Data;

import java.util.List;

/**
 * @author liyuan
 * @date 2022/11/10
 * @project exam-cloud
 */
@Data
public class MenuPermission {
    private String menu;
    private List<String> rolePermission;
    private List<PermissionDic> permissionDic;
}
