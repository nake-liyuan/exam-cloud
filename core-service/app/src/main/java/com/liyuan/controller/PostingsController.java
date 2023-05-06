package com.liyuan.controller;

import cn.easyes.core.biz.SAPageInfo;
import com.liyuan.api.Result;
import com.liyuan.domain.Postings;
import com.liyuan.domain.UserState;
import com.liyuan.domain.app.params.PostingsParams;
import com.liyuan.domain.app.params.ViewPostingsParams;
import com.liyuan.domain.app.vo.postings.CircleInfoVo;
import com.liyuan.domain.app.vo.postings.CirclePointsRanking;
import com.liyuan.domain.app.vo.postings.HotCircleInfoVo;
import com.liyuan.service.PostingsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author liyuan
 * @date 2023/2/12
 * @project exam-cloud
 */
@SuppressWarnings({"unchecked","rawtypes"})
@RestController
@RequestMapping("postings")
@RequiredArgsConstructor
public class PostingsController {

    private final PostingsService postingsService;

    /**
     * @description 展示圈子排名
     */
    @GetMapping("/circleRanking")
    public Result getCircleRanking(){
        HashMap<String, Object> circleRanking = postingsService.getCircleRanking();
        return Result.success(circleRanking);
    }

    /**
     * @description 展示圈子排名信息
     */
    @PostMapping("/circleRankingInfo")
    public Result getCircleRankingInfo(@RequestBody CirclePointsRanking circlePointsRanking){
        HashMap<String, Object> circleRankingInfo = postingsService.getCircleRankingInfo(circlePointsRanking);
        return Result.success(circleRankingInfo);
    }

    /**
     * @description 展示热门圈子
     */
    @GetMapping("/circle")
    public Result getHotCircle(){
        List<HotCircleInfoVo> hotCircle = postingsService.getHotCircle();
        return Result.success(hotCircle);
    }
    /**
     * @description 根据ID查询圈子信息
     */
    @GetMapping("/circleById")
    public Result getHotCircle(@RequestParam String id){
        CircleInfoVo circleInfoById = postingsService.getCircleInfoById(id);
        return Result.success(circleInfoById);
    }

    /**
     * @description 展示帖子信息
     * @date
     */
    @PostMapping
    public Result getPostings(@RequestBody PostingsParams postingsParams){
        SAPageInfo postings = postingsService.getPostings(postingsParams);
        return Result.success(postings);
    }

    /**
     * @description 展示圈子信息
     * @date
     */
    @PostMapping("/getCircle")
    public Result getCircle(@RequestBody PostingsParams postingsParams){
        SAPageInfo postings = postingsService.getCircle(postingsParams);
        return Result.success(postings);
    }

    /**
     * @description 设置用户帖子状态:(点赞，踩，关注)
     * @param clickState true->确认,false->取消
     * @param optionType 0->踩,1->赞,2->关注圈子,3->关注用户
     * @date
     */
    @PutMapping("/userPostingsState")
    public Result setUserPostingsState(@RequestParam Boolean clickState,
                                       @RequestParam Integer optionType,
                                       @RequestBody UserState userState,
                                       @RequestParam(required = false) String userName,
                                       @RequestParam(required = false) String userImage){
        postingsService.setUserPostingsState(clickState,optionType,userState,userName,userImage);
        return Result.success();
    }

    @DeleteMapping("/circle")
    public Result deleteCircle(@RequestParam String id){
        postingsService.deleteCircle(id);
        return Result.success();
    }

    /**
     * @description 更新最新观看用户用户
     * @param id 帖子id
     * @date
     */
    @PutMapping("/viewUserImage")
    public Result updateViewUserImage(@RequestParam String id,
                                      @RequestBody ViewPostingsParams params){
        postingsService.updateViewPostingsUser(id, params);
        return Result.success();
    }

    @PostMapping("/circle")
    public Result establishCircle(@RequestBody Postings postings){
        boolean save = postingsService.establishCircle(postings);
        return Result.judge(save);
    }

    @PostMapping("/postings")
    public Result establishPostings(@RequestBody Postings postings){
        boolean save = postingsService.establishPostings(postings);
        return Result.judge(save);
    }

    @PostMapping("/userSearches")
    public Result saveUserSearchesValue(@RequestParam String value){
        Result result = postingsService.saveUserSearchesValue(value);
        return result;
    }

    @GetMapping("/userLatelySearches")
    public Result selectUserLatelySearchesValue(){
        Result result = postingsService.selectUserLatelySearchesValue();
        return result;
    }

    @DeleteMapping("/userSearches")
    public Result deleteUserSearches(){
        Result result = postingsService.deleteUserSearches();
        return result;
    }

    @GetMapping("/bloggerInfo")
    public Result getBloggerInfo(@RequestParam String userName){
        Result bloggerInfo = postingsService.getBloggerInfo(userName);
        return bloggerInfo;
    }

    @GetMapping("/getMyCircleIndex")
    public Result getMyCircleIndex(){
        Result myCircleIndex = postingsService.getMyCircleIndex();
        return myCircleIndex;
    }

    /**
     * @description: 按id删除帖子
     * @time: 2023/5/2 22:48
     * @param id 帖子ID
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @DeleteMapping("/delete_post_by_id")
    public Result deletePost(@RequestParam String id){
         postingsService.deletePost(id);
        return Result.success();
    }

    /**
     * @description: 添加帖子浏览计数
     * @time: 2023/5/3 18:01
     * @param id 帖子ID
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @PutMapping("/add_browse_count")
    @ApiOperation(value = "添加帖子浏览计数")
    public Result addPostBrowseCount(@ApiParam("帖子id") @RequestParam String id) {
        postingsService.addPostBrowseCount(id);
        return Result.success();
    }

    @GetMapping("/establish_circle")
    @ApiOperation(value = "用户创建的圈子")
    public Result<HashMap<String, Object>> circleCreatedByUsers() {
        HashMap<String, Object> map = postingsService.establishByUsers();
        return Result.success(map);
    }

    @GetMapping("/follow_circle")
    @ApiOperation(value = "用户关注的圈子")
    public Result<HashMap<String, Object>> joinCircle() {
        HashMap<String, Object> map = postingsService.joinByUsers();
        return Result.success(map);
    }


    @GetMapping("/is_establish_circle")
    @ApiOperation(value = "验证建立圈子")
    public Result<Boolean> verificationEstablishCircle(@ApiParam("帖子id") @RequestParam String id) {
        Boolean aBoolean = postingsService.verificationEstablishCircle(id);
        return Result.success(aBoolean);
    }


}
