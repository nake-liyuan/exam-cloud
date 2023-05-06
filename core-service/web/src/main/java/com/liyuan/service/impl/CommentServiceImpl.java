package com.liyuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.Comment;
import com.liyuan.mapper.mp.CommentMapper;
import com.liyuan.service.CommentService;
import org.springframework.stereotype.Service;
/**
* @author liyuan
*@date 2023/2/20
*@project exam-cloud
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService{

}
