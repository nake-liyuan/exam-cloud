package com.liyuan.domain.vo;

import com.liyuan.typeHandler.Hidden;
import lombok.Data;

/**
 * @author liyuan
 * @date 2022/10/24
 * @project exam-cloud
 */
@Data
public class MenusVo {
    private String path;
    private Meta meta;
    private String name;
    private String type;
    private Hidden hidden;
    private String sort;
    private String id;
    private String pid;
}
