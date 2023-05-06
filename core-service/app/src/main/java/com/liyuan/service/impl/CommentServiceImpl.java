package com.liyuan.service.impl;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.api.Result;
import com.liyuan.constant.AuthConstant;
import com.liyuan.domain.Comment;
import com.liyuan.domain.Postings;
import com.liyuan.domain.app.vo.postings.CommentVo;
import com.liyuan.mapper.ee.EsPostingsMapper;
import com.liyuan.mapper.mp.CommentMapper;
import com.liyuan.service.CommentService;
import com.liyuan.service.PostingsService;
import com.liyuan.service.RedisService;
import com.liyuan.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyuan
 * @date 2023/2/20
 * @project exam-cloud
 */
@SuppressWarnings({"rawtypes"})
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final CommentMapper commentMapper;
    private final RedisService redisService;
    private final EsPostingsMapper esPostingsMapper;
    private final PostingsService postingsService;

    @Override
    public Result upfateUserLikeCommentState(String id, String postingsId) {
        Boolean member = redisService.sIsMember(AuthConstant.USER_LIKE_POSTINGS_COMMENT + UserUtils.getUserId() + ":" + postingsId, id);
        if(member){
            redisService.sRemove(AuthConstant.USER_LIKE_POSTINGS_COMMENT+ UserUtils.getUserId()+":"+postingsId,id);
            this.update(new UpdateWrapper<Comment>()
                    .eq("id", id)
                    .setSql("like_num = like_num - 1")
            );
        }else {
            redisService.sAdd(AuthConstant.USER_LIKE_POSTINGS_COMMENT+ UserUtils.getUserId()+":"+postingsId,id);
            this.update(new UpdateWrapper<Comment>()
                    .eq("id", id)
                    .setSql("like_num = like_num + 1")
            );
        }
        return Result.success();
    }

    @Override
    public HashMap<String,Object> getPostingsDetailsData(String id) {
        String content = esPostingsMapper.selectOne(
                new LambdaEsQueryWrapper<Postings>()
                        .select(Postings::getContent)
                        .eq(Postings::getId, id)
        ).getContent();
        List<Comment> commentList = commentMapper.selectList(
                new QueryWrapper<Comment>()
                        .eq("parent_id", id)
        );
        List<CommentVo> comment=new ArrayList<>();
        if(!commentList.isEmpty()){
            List<String> ids = commentList.stream().map(Comment::getId).collect(Collectors.toList());
            commentList.addAll(commentMapper.selectList(
                    new QueryWrapper<Comment>()
                            .in("parent_id", ids)
            ));
            HashSet hashSet = (HashSet) redisService.sMembers(AuthConstant.USER_LIKE_POSTINGS_COMMENT + UserUtils.getUserId() + ":" + id);
                comment = commentList.stream().map(e -> {
                CommentVo commentVo = new CommentVo();
                BeanUtils.copyProperties(e,commentVo);
                commentVo.setOwner(UserUtils.getUserId().equals(e.getUid()));
                commentVo.setHasLike(hashSet.contains(e.getId()));
                return commentVo;
            }).collect(Collectors.toList());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("content",content);
        map.put("comment",comment);
        return map;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteComment(String id, String postingsId) {
        boolean remove = this.removeById(id);
        if(remove){
           postingsService.update(new UpdateWrapper<Postings>()
                    .eq("id", postingsId)
                    .setSql("comment_count = comment_count - 1")
            );
            redisService.sRemove(AuthConstant.USER_LIKE_POSTINGS_COMMENT+ UserUtils.getUserId()+":"+postingsId,id);
        }
        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result saveComment(Comment comment,String postingsId) {
        boolean save = this.save(comment);
        if(save){
            postingsService.update(new UpdateWrapper<Postings>()
                    .eq("id", postingsId)
                    .setSql("comment_count = comment_count + 1")
            );
        }
        return Result.success();
    }

}
