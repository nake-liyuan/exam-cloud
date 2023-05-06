package com.liyuan.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyuan.domain.Menu;
import com.liyuan.domain.vo.MenusVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
* @author liyuan
*@date 2022/10/24
*@project exam-cloud
*/
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenusVo> getMenuByRole(@Param("roles") ArrayList<String> roles);

    List<Menu> listMenus(@Param("title") String title);

    ArrayList<String> listMenusId(@Param("id") String id);

}