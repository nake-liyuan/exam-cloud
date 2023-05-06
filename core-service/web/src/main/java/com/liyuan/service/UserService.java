package com.liyuan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.domain.User;
import com.liyuan.domain.vo.RoleParams;
import com.liyuan.domain.vo.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* @author liyuan
*@date 2022/10/15
*@project exam-cloud
*/
public interface UserService extends IService<User>{

    HashMap<String,String> getUserInfo();

    List<RoleParams> roleParams();

    Page<UserInfo> listUserPage(Integer page, Integer size, String value, Integer status);

    void setAdminAccountInfo(User user);

    void setOwnAccountInfo(User user);

    boolean editUser(UserInfo userInfo);

    boolean delete(ArrayList<String> ids);

    boolean logout();
}
