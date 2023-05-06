package com.liyuan.service.impl;

import cn.easyes.core.biz.SAPageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.api.Result;
import com.liyuan.constant.AuthConstant;
import com.liyuan.domain.Postings;
import com.liyuan.domain.User;
import com.liyuan.domain.UserState;
import com.liyuan.domain.app.params.PostingsParams;
import com.liyuan.domain.app.params.ViewPostingsParams;
import com.liyuan.domain.app.vo.BloggerInfoVo;
import com.liyuan.domain.app.vo.postings.*;
import com.liyuan.mapper.ee.EsPostingsMapper;
import com.liyuan.mapper.mp.PostingsMapper;
import com.liyuan.mapper.mp.UserMapper;
import com.liyuan.mapper.mp.UserStateMapper;
import com.liyuan.service.PostingsService;
import com.liyuan.service.RedisService;
import com.liyuan.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liyuan
 * @date 2023/2/11
 * @project exam-cloud
 */
@Service
@SuppressWarnings({"unchecked","rawtypes"})
@RequiredArgsConstructor
public class PostingsServiceImpl extends ServiceImpl<PostingsMapper, Postings> implements PostingsService {

    private final EsPostingsMapper esPostingsMapper;
    private final RedisService redisService;
    private final RedisTemplate redisTemplate;
    private final UserStateMapper userStateMapper;
    private final UserMapper userMapper;

    @Override
    public List<HotCircleInfoVo> getHotCircle() {
        List<Postings> postings = esPostingsMapper.selectList(
                new LambdaEsQueryWrapper<Postings>()
                        .select(Postings::getId, Postings::getCircleName, Postings::getFollowCount, Postings::getMainImage)
                        .eq(Postings::getPid, 0)
                        .eq(Postings::getIsDeleted,0)
                        .limit(6)
                        .orderByDesc(Postings::getFollowCount)
        );
        List<HotCircleInfoVo> collect = postings.stream().map(e -> {
            HotCircleInfoVo hotCircleInfoVo = new HotCircleInfoVo();
            hotCircleInfoVo.setId(e.getId());
            hotCircleInfoVo.setImage(e.getMainImage().split(",")[0]);
            hotCircleInfoVo.setName(e.getCircleName());
            hotCircleInfoVo.setCount(e.getFollowCount());
            return hotCircleInfoVo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public CircleInfoVo getCircleInfoById(String id) {
        Postings postings = esPostingsMapper.selectOne(
                new LambdaEsQueryWrapper<Postings>()
                        .select(Postings::getMainImage, Postings::getUserImage, Postings::getCircleName, Postings::getDesc)
                        .eq(Postings::getId, id)
        );
        CircleInfoVo circleInfoVo = new CircleInfoVo();
        Boolean member = redisService.sIsMember(AuthConstant.USER_JOIN_CIRCLE + UserUtils.getUserId(), id);
        if (member) {
            circleInfoVo.setFollowState(1);
        } else {
            circleInfoVo.setFollowState(0);
        }

        circleInfoVo.setName(postings.getCircleName());
        circleInfoVo.setSwiperList(Arrays.asList(postings.getMainImage().split(",")));
        circleInfoVo.setMainImage(postings.getUserImage());
        circleInfoVo.setDesc(postings.getDesc());
        return circleInfoVo;
    }

    @Override
    public SAPageInfo getPostings(PostingsParams postingsParams) {
        if (postingsParams.getSearchMyCircle()) {
            HashSet hashSet = (HashSet) redisService.sMembers(AuthConstant.USER_JOIN_CIRCLE + UserUtils.getUserId());
            ArrayList<String> pid = new ArrayList<String>(hashSet);
            postingsParams.setPid(pid);
            if(postingsParams.getPid().isEmpty()){
                SAPageInfo<Object> emptySAPageInfo = new SAPageInfo<>();
                emptySAPageInfo.setPages(0);
                emptySAPageInfo.setPageSize(0);
                emptySAPageInfo.setList(new ArrayList<>());
                emptySAPageInfo.setNextSearchAfter(new ArrayList<>());
                return emptySAPageInfo;
            }
        }
        SAPageInfo<Postings> saPageInfo = esPostingsMapper.searchAfterPage(
                new LambdaEsQueryWrapper<Postings>()
                        .select(Postings::getId, Postings::getUsername, Postings::getUserImage, Postings::getCreateTime,
                                Postings::getLabel, Postings::getDesc, Postings::getMainImage,
                                Postings::getTrampleCount, Postings::getLikeCount, Postings::getCommentCount, Postings::getViewCount
                        )
                        .eq(Postings::getIsDeleted,0)
                        .ne(Postings::getPid, 0)
                        .in(!postingsParams.getPid().isEmpty(), Postings::getPid, postingsParams.getPid())
                        .multiMatchQuery(
                                !postingsParams.getSearchMyPostings() && StringUtils.hasText(postingsParams.getValue()), postingsParams.getValue(),
                                Postings::getLabel, Postings::getDesc, Postings::getUsername
                        )
                        .in(postingsParams.getSearchMyPostings(), Postings::getUid,UserUtils.getUserId())
                        .and(postingsParams.getOrderState() == 0, postingsLambdaEsQueryWrapper -> postingsLambdaEsQueryWrapper
                                .orderByDesc(Postings::getCreateTime, Postings::getLikeCount, Postings::getViewCount)
                        )
                        .and(postingsParams.getOrderState() == 1, postingsLambdaEsQueryWrapper -> postingsLambdaEsQueryWrapper
                                .orderByDesc(Postings::getLikeCount, Postings::getCreateTime, Postings::getViewCount)
                        )
                        .and(postingsParams.getOrderState() == 2, postingsLambdaEsQueryWrapper -> postingsLambdaEsQueryWrapper
                                .orderByDesc(Postings::getViewCount, Postings::getCreateTime, Postings::getViewCount)
                        )
                , postingsParams.getNextSearchAfter(), postingsParams.getPageSize());

        //获取用户点赞帖子id
        HashSet<String> userLikePostingsId = new HashSet<String>();
        Boolean hasLike = redisService.hasKey(AuthConstant.USER_LIKE_POSTINGS + UserUtils.getUserId());
        if (hasLike) {
            userLikePostingsId = (HashSet) redisService.sMembers(AuthConstant.USER_LIKE_POSTINGS + UserUtils.getUserId());
        }

        //获取用户踩帖子id
        HashSet<String> userTramplePostingsId = new HashSet<String>();
        Boolean hasTrample = redisService.hasKey(AuthConstant.USER_TRAMPLE_POSTINGS + UserUtils.getUserId());
        if (hasTrample) {
            userTramplePostingsId = (HashSet) redisService.sMembers(AuthConstant.USER_TRAMPLE_POSTINGS + UserUtils.getUserId());
        }

        HashSet<String> finalUserLikePostingsId = userLikePostingsId;
        HashSet<String> finalUserTramplePostingsId = userTramplePostingsId;
        List<PostingsVo> collect = saPageInfo.getList().stream().map(e -> {
            PostingsVo postingsVo = new PostingsVo();
            postingsVo.setId(e.getId());
            postingsVo.setHighlightUsername(StringUtils.hasText(e.getHighlightUsername()) ? e.getHighlightUsername() : e.getUsername());
            postingsVo.setUsername(e.getUsername());
            postingsVo.setUserImage(e.getUserImage());
            postingsVo.setReleaseDate(e.getCreateTime());
            postingsVo.setLabel(Arrays.asList(e.getLabel().split(",")));
            postingsVo.setDesc(e.getDesc());
            postingsVo.setMainImage(Arrays.asList(e.getMainImage().split(",")));
            postingsVo.setLikeState(finalUserLikePostingsId.contains(e.getId()) ? 1 : 0);
            postingsVo.setLikeCount(e.getLikeCount());
            postingsVo.setTrampleState(finalUserTramplePostingsId.contains(e.getId()) ? 1 : 0);
            postingsVo.setTrampleCount(e.getTrampleCount());
            postingsVo.setCommentCount(e.getCommentCount());
            postingsVo.setLatestViewUserAvatar((HashSet<String>) redisTemplate.boundZSetOps(AuthConstant.USER_VIEW_POSTINGS + e.getId()).reverseRange(0, 3));
            postingsVo.setViewUserCount(e.getViewCount());
            return postingsVo;
        }).collect(Collectors.toList());
        SAPageInfo saPageInfo1 = new SAPageInfo();
        BeanUtils.copyProperties(saPageInfo, saPageInfo1);
        saPageInfo1.setList(collect);
        return saPageInfo1;
    }

    @Override
    public SAPageInfo getCircle(PostingsParams postingsParams) {
        SAPageInfo<Postings> saPageInfo = esPostingsMapper.searchAfterPage(
                new LambdaEsQueryWrapper<Postings>()
                        .eq(Postings::getPid, 0)
                        .eq(Postings::getIsDeleted,0)
                        .select(Postings::getId, Postings::getUsername, Postings::getUserImage, Postings::getMainImage,
                                Postings::getCircleName, Postings::getFollowCount, Postings::getCreateTime)
                        .multiMatchQuery(
                                StringUtils.hasText(postingsParams.getValue()), postingsParams.getValue(),
                                Postings::getLabel, Postings::getCircleName, Postings::getUsername
                        )
                        .eq(postingsParams.getSearchMyCircle(),Postings::getUid,UserUtils.getUserId())
                        .and(postingsParams.getOrderState() == 0, postingsLambdaEsQueryWrapper -> postingsLambdaEsQueryWrapper
                                .orderByDesc(Postings::getCreateTime, Postings::getFollowCount)
                        )
                        .and(postingsParams.getOrderState() == 1, postingsLambdaEsQueryWrapper -> postingsLambdaEsQueryWrapper
                                .orderByDesc(Postings::getFollowCount, Postings::getCreateTime)
                        )
                , postingsParams.getNextSearchAfter(), postingsParams.getPageSize());
        List<CircleVo> collect = saPageInfo.getList().stream().map(e -> {
            CircleVo circleVo = new CircleVo();
            circleVo.setId(e.getId());
            circleVo.setMainImage(e.getMainImage().split(",")[0]);
            circleVo.setName(e.getCircleName());
            circleVo.setUserImage(e.getUserImage());
            circleVo.setUserName(e.getUsername());
            circleVo.setCount(e.getFollowCount());
            circleVo.setReleaseDate(e.getCreateTime());
            return circleVo;
        }).collect(Collectors.toList());
        SAPageInfo circleInfo = new SAPageInfo();
        BeanUtils.copyProperties(saPageInfo, circleInfo);
        circleInfo.setList(collect);
        return circleInfo;
    }

    /**
     * @param clickState true->确认,false->取消
     * @param optionType 0->踩,1->赞,2->关注圈子,3->关注用户
     * @description 设置用户帖子状态:(点赞，踩，关注)
     * @date
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setUserPostingsState(Boolean clickState, Integer optionType, UserState userState, String userName, String userImage) {
        if (clickState) {
            switch (optionType) {
                case 0:
                    //踩帖子
                    redisService.sAdd(AuthConstant.USER_TRAMPLE_POSTINGS + UserUtils.getUserId(), userState.getPostingsId());
                    this.update(new UpdateWrapper<Postings>()
                            .eq("id", userState.getPostingsId())
                            .setSql("trample_count = trample_count + 1")
                    );
                    break;
                case 1:
                    //点赞帖子
                    redisService.sAdd(AuthConstant.USER_LIKE_POSTINGS + UserUtils.getUserId(), userState.getPostingsId());
                    this.update(new UpdateWrapper<Postings>()
                            .eq("id", userState.getPostingsId())
                            .setSql("like_count = like_count + 1")
                    );
                    break;
                case 2:
                    //关注圈子
                    redisService.sAdd(AuthConstant.USER_JOIN_CIRCLE + UserUtils.getUserId(), userState.getPostingsId());
                    Postings one = this.getOne(new QueryWrapper<Postings>().eq("id", userState.getPostingsId()).select("uid", "username", "user_image"));
                    CirclePointsRanking circlePointsRanking = new CirclePointsRanking();
                    circlePointsRanking.setUid(one.getUid());
                    circlePointsRanking.setUserName(one.getUsername());
                    circlePointsRanking.setUserImage(one.getUserImage());
                    //关注圈子分数加100
                    redisTemplate.boundZSetOps(AuthConstant.USER_CIRCLE_RANKING).incrementScore(circlePointsRanking, 100D);
                    this.update(new UpdateWrapper<Postings>()
                            .eq("id", userState.getPostingsId())
                            .setSql("follow_count = follow_count + 1")
                    );
                    break;
                case 3:
                    //关注用户
                    redisService.sAdd(AuthConstant.USER_FOLLOW_USER + userState.getUid(), userState.getFollowUid());
                    redisService.sAdd(AuthConstant.USER_FANS_USER + userState.getFollowUid(), userState.getUid());
                    break;
                default:
                    break;
            }
            userStateMapper.insert(userState);
        } else {
            switch (optionType) {
                case 0:
                    //取消踩
                    redisService.sRemove(AuthConstant.USER_TRAMPLE_POSTINGS + UserUtils.getUserId(), userState.getPostingsId());
                    this.update(new UpdateWrapper<Postings>()
                            .eq("id", userState.getPostingsId())
                            .setSql("trample_count = trample_count - 1")
                    );
                    break;
                case 1:
                    //取消点赞
                    redisService.sRemove(AuthConstant.USER_LIKE_POSTINGS + UserUtils.getUserId(), userState.getPostingsId());
                    this.update(new UpdateWrapper<Postings>()
                            .eq("id", userState.getPostingsId())
                            .setSql("like_count = like_count - 1")
                    );
                    break;
                case 2:
                    //取消圈子关注
                    redisService.sRemove(AuthConstant.USER_JOIN_CIRCLE + UserUtils.getUserId(), userState.getPostingsId());
                    Postings one = this.getOne(new QueryWrapper<Postings>().eq("id", userState.getPostingsId()).select("uid", "username", "user_image"));
                    CirclePointsRanking circlePointsRanking = new CirclePointsRanking();
                    circlePointsRanking.setUid(one.getUid());
                    circlePointsRanking.setUserName(one.getUsername());
                    circlePointsRanking.setUserImage(one.getUserImage());
                    //取消圈子关注-100
                    redisTemplate.boundZSetOps(AuthConstant.USER_CIRCLE_RANKING).incrementScore(circlePointsRanking, -100D);
                    this.update(new UpdateWrapper<Postings>()
                            .eq("id", userState.getPostingsId())
                            .setSql("follow_count = follow_count - 1")
                    );
                    break;
                case 3:
                    //取消用户关注
                    redisService.sRemove(AuthConstant.USER_FOLLOW_USER + userState.getUid(), userState.getFollowUid());
                    redisService.sRemove(AuthConstant.USER_FANS_USER + userState.getFollowUid(), userState.getUid());
                    break;
                default:
                    break;
            }
            userStateMapper.delete(
                    new QueryWrapper<UserState>()
                            .eq(StringUtils.hasText(userState.getPostingsId()),"postings_id", userState.getPostingsId())
                            .eq("uid", userState.getUid())
                            .eq(StringUtils.hasText(userState.getFollowUid()),"follow_uid",userState.getFollowUid())
            );
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCircle(String id) {
        //取消圈子关注
        redisService.sRemove(AuthConstant.USER_JOIN_CIRCLE + UserUtils.getUserId(),id);
        redisService.sRemove(AuthConstant.USER_ESTABLISH_CIRCLE + UserUtils.getUserId(),id);
        userStateMapper.delete(
                new QueryWrapper<UserState>()
                        .eq("postings_id", id)
                        .eq("uid", UserUtils.getUserId())
        );
        boolean removeById = this.removeById(id);
        if(removeById){
            this.remove(new QueryWrapper<Postings>().eq("pid",id));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateViewPostingsUser(String id, ViewPostingsParams params) {
        redisTemplate.opsForZSet().incrementScore(AuthConstant.USER_VIEW_POSTINGS + id, params, System.currentTimeMillis());
        UserState userState = new UserState();
        userState.setPostingsId(id);
        userState.setUid(params.getUid());
        userState.setViewState(1);
        userStateMapper.insert(userState);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean establishCircle(Postings postings) {
        String uid=UserUtils.getUserId();
        postings.setUid(uid);
        postings.setFollowCount(1);
        boolean save = this.save(postings);
        redisService.sAdd(AuthConstant.USER_ESTABLISH_CIRCLE + UserUtils.getUserId(), postings.getId());
        if (save) {
            UserState userState = new UserState();
            userState.setUid(uid);
            userState.setPostingsId(postings.getId());
            userState.setCircleFollowState(1);
            this.userStateMapper.insert(userState);
            redisService.sAdd(AuthConstant.USER_JOIN_CIRCLE + UserUtils.getUserId(), postings.getId());
            CirclePointsRanking circlePointsRanking = new CirclePointsRanking();
            circlePointsRanking.setUid(uid);
            circlePointsRanking.setUserName(postings.getUsername());
            circlePointsRanking.setUserImage(postings.getUserImage());
            //创建圈子，初始化排名，分数小数位
            Double score = redisTemplate.opsForZSet().score(AuthConstant.USER_CIRCLE_RANKING, circlePointsRanking);
            if (score != null) {
                redisTemplate.boundZSetOps(AuthConstant.USER_CIRCLE_RANKING).incrementScore(circlePointsRanking, 100D);
            }else {
                redisTemplate.boundZSetOps(AuthConstant.USER_CIRCLE_RANKING).add(circlePointsRanking,this.getScoreByTime(System.currentTimeMillis()).doubleValue());
            }

        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean establishPostings(Postings postings) {
        postings.setUid(UserUtils.getUserId());
        boolean save = this.save(postings);
        if (save) {
            redisService.sAdd(AuthConstant.USER_RELEASE_POSTINGS + UserUtils.getUserId(), postings.getId());
        }
        return true;
    }

    @Override
    public HashMap<String, Object> getCircleRanking() {
        //排名总数
        Long total = redisTemplate.boundZSetOps(AuthConstant.USER_CIRCLE_RANKING).size();
        //排前3名信息
        Set set = redisTemplate.boundZSetOps(AuthConstant.USER_CIRCLE_RANKING).reverseRange(0, 3);
        HashMap<String, Object> getCircleRanking = new HashMap<>();
        getCircleRanking.put("total", total);
        getCircleRanking.put("data", set);
        return getCircleRanking;
    }

    @Override
    public HashMap<String, Object> getCircleRankingInfo(CirclePointsRanking circlePointsRanking) {
        //前20
        Set rankInfo = redisTemplate.boundZSetOps(AuthConstant.USER_CIRCLE_RANKING).reverseRangeWithScores(0L, 19L);
        //用户排名
        Long rank = redisTemplate.opsForZSet().rank(AuthConstant.USER_CIRCLE_RANKING, circlePointsRanking);
        //用户得分
        Double score = redisTemplate.opsForZSet().score(AuthConstant.USER_CIRCLE_RANKING, circlePointsRanking);
        HashMap<String, Object> getCircleRankingInfo = new HashMap<>();
        getCircleRankingInfo.put("rankInfo", rankInfo);
        getCircleRankingInfo.put("userRank", (rank==null?-1:rank+1) );
        getCircleRankingInfo.put("userScore", score);
        return getCircleRankingInfo;
    }

    @Override
    public Result saveUserSearchesValue(String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put("title", value);
        redisTemplate.boundZSetOps(AuthConstant.USER_SEARCHES_VALUE + UserUtils.getUserId()).add(map, System.currentTimeMillis());
        return Result.success();
    }

    @Override
    public Result selectUserLatelySearchesValue() {
        Set set = redisTemplate.boundZSetOps(AuthConstant.USER_SEARCHES_VALUE + UserUtils.getUserId()).reverseRange(0, 9);
        return Result.success(set);
    }

    @Override
    public Result deleteUserSearches() {
        Boolean del = redisService.del(AuthConstant.USER_SEARCHES_VALUE + UserUtils.getUserId());
        return Result.judge(del);
    }

    @Override
    public Result getBloggerInfo(String username) {

        User user = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("name", username)
                        .select("id", "name", "phone", "email", "head_portrait")
        );
        BloggerInfoVo bloggerInfoVo = new BloggerInfoVo();
        bloggerInfoVo.setId(user.getId());
        bloggerInfoVo.setUserName(user.getName());
        bloggerInfoVo.setUserImage(user.getHeadPortrait());
        bloggerInfoVo.setPhone(user.getPhone());
        bloggerInfoVo.setEmail(user.getEmail());
        Boolean hasLike = redisService.hasKey(AuthConstant.USER_RELEASE_POSTINGS + user.getId());
        if (hasLike) {
            HashSet<String> userLikePostingsId = (HashSet) redisService.sMembers(AuthConstant.USER_RELEASE_POSTINGS + user.getId());
            Integer integer = userStateMapper.selectCount(
                    new QueryWrapper<UserState>()
                            .in("postings_id", userLikePostingsId)
            );
            bloggerInfoVo.setLikeNum(integer);
        } else {
            bloggerInfoVo.setLikeNum(0);
        }
        CirclePointsRanking circlePointsRanking = new CirclePointsRanking();
        circlePointsRanking.setUid(user.getId());
        circlePointsRanking.setUserImage(user.getHeadPortrait());
        circlePointsRanking.setUserName(username);
        //用户得分
        Double score = redisTemplate.opsForZSet().score(AuthConstant.USER_CIRCLE_RANKING, circlePointsRanking);
        bloggerInfoVo.setScore(score);
        bloggerInfoVo.setFollowNum(redisService.sSize(AuthConstant.USER_FOLLOW_USER + user.getId()));
        bloggerInfoVo.setFansNum(redisService.sSize(AuthConstant.USER_FANS_USER + user.getId()));
        bloggerInfoVo.setFollowUser(redisService.sIsMember(AuthConstant.USER_FOLLOW_USER + UserUtils.getUserId(),user.getId()));
        return Result.success(bloggerInfoVo);
    }

    @Override
    public Result getMyCircleIndex() {
        HashSet hashSet = (HashSet) redisService.sMembers(AuthConstant.USER_JOIN_CIRCLE + UserUtils.getUserId());
        List<Postings> postings = esPostingsMapper.selectList(
                new LambdaEsQueryWrapper<Postings>()
                        .select(Postings::getId,Postings::getCircleName, Postings::getMainImage)
                        .in(Postings::getId, hashSet)
        );
        HashSet<String> indexList = new HashSet();
        HashMap<String, List<Postings>> map = new HashMap<>();
        postings.forEach(e->{
            String firstLetter = PinyinUtil.getFirstLetter(e.getCircleName().substring(0, 1), "");
            if(map.containsKey(firstLetter)){
                List<Postings> postingsList = map.get(firstLetter);
                postingsList.add(e);
                map.put(firstLetter,postingsList);
            }else {
                indexList.add(PinyinUtil.getFirstLetter(e.getCircleName().substring(0,1), ""));
                List<Postings> initPostingsList = new ArrayList<>();
                initPostingsList.add(e);
                map.put(firstLetter,initPostingsList);
            }
        });
        List<HashMap<String, Object>> collect = indexList.stream().map(e -> {
            HashMap<String, Object> data = new HashMap<>();
            data.put("letter", e);
            data.put("data", map.get(e));
            return data;
        }).collect(Collectors.toList());
        HashMap<String, Object> info = new HashMap<>();
        info.put("indexList",indexList);
        info.put("list",collect);
        return Result.success(info);
    }

    @Override
    public void deletePost(String id) {
        this.removeById(id);
    }

    @Override
    public void addPostBrowseCount(String id) {
        this.update(new UpdateWrapper<Postings>().eq("id",id).setSql("view_count = view_count + 1"));
    }

    @Override
    public HashMap<String ,Object> establishByUsers() {
        HashMap<String, Object> data = new HashMap<>();
        HashSet establishCircleIds = (HashSet) redisService.sMembers(AuthConstant.USER_ESTABLISH_CIRCLE + UserUtils.getUserId());
        if(establishCircleIds.isEmpty()){
            data.put("indexList",new ArrayList<>());
            data.put("list",new ArrayList<>());
        }else {
            List<Postings> establishCircleData = esPostingsMapper.selectList(
                    new LambdaEsQueryWrapper<Postings>()
                            .select(Postings::getId,Postings::getCircleName, Postings::getMainImage,Postings::getFollowCount)
                            .in(Postings::getId, establishCircleIds)
            );
            HashMap<String, Object> userEstablishCircleIndexList = createCircleIndexList(establishCircleData);
            data.put("indexList",userEstablishCircleIndexList.get("indexList"));
            data.put("list",userEstablishCircleIndexList.get("list"));
        }

        return data;
    }

    public HashMap<String ,Object> joinByUsers() {
        HashMap<String, Object> data = new HashMap<>();
        HashSet establishCircleIds = (HashSet) redisService.sMembers(AuthConstant.USER_ESTABLISH_CIRCLE + UserUtils.getUserId());
        HashSet JoinCircleIds = (HashSet) redisService.sMembers(AuthConstant.USER_JOIN_CIRCLE + UserUtils.getUserId());
        JoinCircleIds.removeAll(establishCircleIds);
        if(JoinCircleIds.isEmpty()){
            data.put("indexList",new ArrayList<>());
            data.put("list",new ArrayList<>());
        }else {
            List<Postings> userJoinCircleData = esPostingsMapper.selectList(
                    new LambdaEsQueryWrapper<Postings>()
                            .select(Postings::getId,Postings::getCircleName, Postings::getMainImage,Postings::getFollowCount)
                            .in(Postings::getId, JoinCircleIds)
            );
            HashMap<String, Object> userJoinCircleIndexList = createCircleIndexList(userJoinCircleData);
            data.put("indexList",userJoinCircleIndexList.get("indexList"));
            data.put("list",userJoinCircleIndexList.get("list"));
        }
        return data;
    }

    @Override
    public Boolean verificationEstablishCircle(String id) {
        Boolean aBoolean = redisService.sIsMember(AuthConstant.USER_ESTABLISH_CIRCLE + UserUtils.getUserId(), id);
        return aBoolean;
    }


    private BigDecimal getScoreByTime(Long time) {
        return new BigDecimal("1.0").subtract(new BigDecimal(time * Math.pow(10, Math.negateExact(String.valueOf(time).length()))));
    }

    private  HashMap<String, Object> createCircleIndexList(List<Postings> postings){
        postings.forEach(System.out::println);
        HashSet<String> indexList = new HashSet();
        HashMap<String, List<Postings>> map = new HashMap<>();
        postings.forEach(e->{
            String firstLetter = PinyinUtil.getFirstLetter(e.getCircleName().substring(0, 1), "");
            if(map.containsKey(firstLetter)){
                List<Postings> postingsList = map.get(firstLetter);
                postingsList.add(e);
                map.put(firstLetter,postingsList);
            }else {
                indexList.add(PinyinUtil.getFirstLetter(e.getCircleName().substring(0,1), ""));
                List<Postings> initPostingsList = new ArrayList<>();
                initPostingsList.add(e);
                map.put(firstLetter,initPostingsList);
            }
        });
        List<HashMap<String, Object>> collect = indexList.stream().map(e -> {
            HashMap<String, Object> data = new HashMap<>();
            data.put("letter", e);
            data.put("data", map.get(e));
            return data;
        }).collect(Collectors.toList());
        
        HashMap<String, Object> info = new HashMap<>();
        info.put("indexList",indexList);
        info.put("list",collect);
        return info;
    }



}
