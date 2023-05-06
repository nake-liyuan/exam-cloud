package com.liyuan.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyuan.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
* @author liyuan
*@date 2023/2/20
*@project exam-cloud
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}