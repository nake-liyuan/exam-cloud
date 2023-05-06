package com.liyuan.domain.vo;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author liyuan
 * @date 2022/10/24
 * @project exam-cloud
 */
@Data
public class Meta {
    private String title;
    private String icon;
    private String iconType;
    private ArrayList<String> permissions;
}
