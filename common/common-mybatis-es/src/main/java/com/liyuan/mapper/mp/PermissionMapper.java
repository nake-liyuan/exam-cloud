package com.liyuan.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyuan.domain.Permission;
import com.liyuan.domain.vo.UrlRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author liyuan
*@date 2022/10/15
*@project exam-cloud
*/
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    List<UrlRole> initResourceRolesMap();

}