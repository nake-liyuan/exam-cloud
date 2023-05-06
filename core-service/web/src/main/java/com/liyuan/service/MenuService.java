package com.liyuan.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.domain.Menu;
import com.liyuan.domain.Permission;

import java.util.ArrayList;
import java.util.List;

/**
* @author liyuan
*@date 2022/10/24
*@project exam-cloud
*/
public interface MenuService extends IService<Menu>{

    List<Tree<String>> getMenuByRole(ArrayList<String> roles);

    List<Tree<String>> listMenu(String title);

    Page<Permission> listPermissionByMenuId(Integer page, Integer size, String id, String name);

    List<Tree<String>>  listMenuCatalogue();


}
