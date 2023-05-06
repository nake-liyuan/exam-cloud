package com.liyuan.controller.druid;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liyuan
 * @date 2023/3/27
 * @project exam-cloud
 */
@RestController
@RequestMapping(value = "/druid")
public class DruidStatController {

    @GetMapping("/stat")
    public Object druidStat(){
        // 获取数据源的监控数据
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}