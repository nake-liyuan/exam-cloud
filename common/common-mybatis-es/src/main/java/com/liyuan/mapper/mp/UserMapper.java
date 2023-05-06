package com.liyuan.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuan.domain.User;
import com.liyuan.domain.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author liyuan
*@date 2022/10/15
*@project exam-cloud
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    UserVo getUserRoles(String username);

    Page<User> listUser(Page<User> page,@Param("value") String value, @Param("status")Integer status);

}