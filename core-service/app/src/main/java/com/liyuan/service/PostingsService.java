package com.liyuan.service;

import cn.easyes.core.biz.SAPageInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.api.Result;
import com.liyuan.domain.Postings;
import com.liyuan.domain.UserState;
import com.liyuan.domain.app.params.PostingsParams;
import com.liyuan.domain.app.params.ViewPostingsParams;
import com.liyuan.domain.app.vo.postings.CircleInfoVo;
import com.liyuan.domain.app.vo.postings.CirclePointsRanking;
import com.liyuan.domain.app.vo.postings.HotCircleInfoVo;

import java.util.HashMap;
import java.util.List;

/**
* @author liyuan
*@date 2023/2/11
*@project exam-cloud
*/
public interface PostingsService extends IService<Postings>{

    List<HotCircleInfoVo> getHotCircle();

    CircleInfoVo getCircleInfoById(String id);

    SAPageInfo getPostings(PostingsParams postingsParams);

    SAPageInfo getCircle(PostingsParams postingsParams);

    void setUserPostingsState(Boolean clickState, Integer optionType, UserState userState,String userName,String userImage);

    void deleteCircle(String id);

    void updateViewPostingsUser(String id,ViewPostingsParams params);

    Boolean establishCircle(Postings postings);

    Boolean establishPostings(Postings postings);

    HashMap<String,Object> getCircleRanking();

    HashMap<String,Object> getCircleRankingInfo(CirclePointsRanking circlePointsRanking);

    Result saveUserSearchesValue(String value);

    Result selectUserLatelySearchesValue();

    Result deleteUserSearches();

    Result getBloggerInfo(String username);

    Result getMyCircleIndex();

    void deletePost(String id);

    void addPostBrowseCount(String id);

    HashMap<String ,Object> establishByUsers();

    HashMap<String ,Object> joinByUsers();

     Boolean verificationEstablishCircle(String id);


}
