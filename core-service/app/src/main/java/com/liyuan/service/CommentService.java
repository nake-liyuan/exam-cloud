package com.liyuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.api.Result;
import com.liyuan.domain.Comment;

import java.util.HashMap;

/**
 * @author liyuan
 * @date 2023/2/20
 * @project exam-cloud
 */
public interface CommentService extends IService<Comment> {

    Result upfateUserLikeCommentState(String id, String postingsId);

    HashMap<String,Object> getPostingsDetailsData(String id);

    Result deleteComment(String id,String postingsId);

    Result saveComment(Comment comment,String postingsId);

}
