package com.liyuan.component;

import com.liyuan.service.PermRolesRulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author liyuan
 * @date 2022/10/15
 * @project exam-cloud
 */
@Component
@RequiredArgsConstructor
public class InitPermissionRolesCache implements CommandLineRunner {

    private final PermRolesRulesService permRolesRulesService;

    @Override
    public void run(String... args){
        permRolesRulesService.refreshPermRolesRules();
    }
}
