package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.domain.Comment;
import com.liyuan.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author liyuan
 * @date 2023/2/20
 * @project exam-cloud
 */
@SuppressWarnings({"unchecked","rawtypes"})
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public Result getCommmentByPostingsId(@RequestParam String id){
        HashMap<String, Object> postingsDetailsData = commentService.getPostingsDetailsData(id);
        return Result.success(postingsDetailsData);
    }

    @PutMapping
    public Result upfateUserLikeCommentState(@RequestParam String id,
                                             @RequestParam String postingsId){
        Result result = commentService.upfateUserLikeCommentState(id, postingsId);
        return result;
    }

    @DeleteMapping
    public Result deleteComment(@RequestParam String id,@RequestParam String postingsId){
        return commentService.deleteComment(id, postingsId);
    }

    @PostMapping
    public Result saveComment(@RequestBody Comment comment,
                              @RequestParam String postingsId){
        return commentService.saveComment(comment, postingsId);
    }
}
