package com.liyuan.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.Menu;
import com.liyuan.domain.Permission;
import com.liyuan.domain.vo.MenusVo;
import com.liyuan.domain.vo.PermissionKeyValue;
import com.liyuan.mapper.mp.MenuMapper;
import com.liyuan.mapper.mp.PermissionMapper;
import com.liyuan.mapper.mp.RoleMapper;
import com.liyuan.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @author liyuan
*@date 2022/10/24
*@project exam-cloud
*/
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService{

    private final MenuMapper menuMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    @Override
    public List<Tree<String>> getMenuByRole(ArrayList<String> roles) {
        List<MenusVo> menuByRole = menuMapper.getMenuByRole(roles);
        List<PermissionKeyValue> permissionKeyValues = roleMapper.permissionValueByRoles(roles);
            for (int i = 0; i < menuByRole.size(); i++) {
                for (PermissionKeyValue permissionKeyValue : permissionKeyValues) {
                    if(menuByRole.get(i).getId().equals(permissionKeyValue.getMenuId())){
                        menuByRole.get(i).getMeta().setPermissions(permissionKeyValue.getValue());
                    }
                }
            }
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        treeNodeConfig.setIdKey("id");
        // 最大递归深度
        treeNodeConfig.setDeep(5);
        treeNodeConfig.setParentIdKey("pid");
        //转换器
        List<Tree<String>> mens = TreeUtil.build(menuByRole, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setName(treeNode.getName());
                    tree.setWeight(treeNode.getSort());
                    // 扩展属性 ...
                    tree.putExtra("path", treeNode.getPath());
                    tree.putExtra("type", treeNode.getType());
                    tree.putExtra("meta", treeNode.getMeta());
                    tree.putExtra("hidden", treeNode.getHidden().getValue());
                }
        );
        return mens;
    }

    @Override
    public List<Tree<String>> listMenu(String title) {
        List<Menu> menus;
        if (StringUtils.hasText(title)){
            menus = menuMapper.listMenus(title);
        }else {
            menus=menuMapper.selectList(null);
        }

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        treeNodeConfig.setIdKey("id");
        // 最大递归深度
        treeNodeConfig.setDeep(5);
        treeNodeConfig.setParentIdKey("pid");

        //转换器
        List<Tree<String>> treeList = TreeUtil.build(menus, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setWeight(treeNode.getSort());
                    // 扩展属性 ...
                    tree.putExtra("title", treeNode.getTitle());
                    tree.putExtra("icon", treeNode.getIcon());
                    tree.putExtra("hidden", treeNode.getHidden());
                    tree.putExtra("name", treeNode.getName());
                    tree.putExtra("path", treeNode.getPath());
                    tree.putExtra("menuType", treeNode.getMenuType());
                    tree.putExtra("type", treeNode.getType());
                    tree.putExtra("iconType", treeNode.getIcontype());
                }
        );
        return treeList;
    }

    @Override
    public Page<Permission> listPermissionByMenuId(Integer page, Integer size,String id,String name) {
        Page<Permission> Page = new Page<>(page,size);
        ArrayList<String> menusId = null;
            if(StringUtils.hasText(id)){
                 menusId = menuMapper.listMenusId(id);
            }

        Page<Permission> permissionPage = permissionMapper.selectPage(Page,
                new QueryWrapper<Permission>()
                        .in(menusId != null, "menu_id", menusId)
                        .eq(StringUtils.hasText(name), "name", name)
        );
        return permissionPage;
    }

    @Override
    public List<Tree<String>> listMenuCatalogue() {
        List<Menu> menus = menuMapper.selectList(
                new QueryWrapper<Menu>()
                        .select("id", "pid", "title", "sort","path")
                        .eq("menuType",0)
        );
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        treeNodeConfig.setIdKey("id");
        // 最大递归深度
        treeNodeConfig.setDeep(5);
        treeNodeConfig.setParentIdKey("pid");
        //转换器
        List<Tree<String>> treeList = TreeUtil.build(menus, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setWeight(treeNode.getSort());
                    // 扩展属性 ...
                    tree.putExtra("title", treeNode.getTitle());
                    tree.putExtra("path", treeNode.getPath());
                }
        );
        return treeList;
    }


}
