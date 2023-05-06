package com.liyuan.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyuan.domain.UserState;
import com.liyuan.domain.app.vo.postings.HotCircleInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author liyuan
*@date 2023/2/14
*@project exam-cloud
*/
@Mapper
public interface UserStateMapper extends BaseMapper<UserState> {

    @Select("SELECT postings_id AS id, count( * ) AS count  \n" +
            "FROM user_state\n" +
            "WHERE circle_follow_state=1\n" +
            "GROUP BY postings_id  \n" +
            "ORDER BY count DESC  \n" +
            "LIMIT 6  ")
    List<HotCircleInfoVo> getHotCircleIdAndCount();
}