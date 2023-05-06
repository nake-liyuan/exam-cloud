package com.liyuan.domain.vo;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author liyuan
 * @date 2022/11/6
 * @project exam-cloud
 */
@Data
public class PermissionKeyValue {

    private String menuId;

    private ArrayList<String> value;
}
