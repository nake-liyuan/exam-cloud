package com.liyuan.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.constant.AuthConstant;
import com.liyuan.domain.Postings;
import com.liyuan.domain.User;
import com.liyuan.domain.app.vo.postings.CirclePointsRanking;
import com.liyuan.entity.dto.UserOtherInfo;
import com.liyuan.mapper.mp.PostingsMapper;
import com.liyuan.mapper.mp.UserMapper;
import com.liyuan.service.PostingsService;
import com.liyuan.service.UserService;
import com.liyuan.util.UserUtils;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import netscape.security.UserTarget;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
* @author liyuan
*@date 2022/10/15
*@project exam-cloud
*/
@SuppressWarnings({"unchecked","rawtypes"})
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{


    private final UserMapper userMapper;
    private final PostingsMapper postingsMapper;
    private final RedisTemplate redisTemplate;

    @Override
    public User getUserInfo() {
        String userId = UserUtils.getUserId();
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("id", userId)
                .select("id", "name", "head_portrait", "phone")
        );
        return user;
    }

    @Override
    public User getuserOtherInfo() {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", UserUtils.getUserId()).select("phone", "email", "education"));
        return user;
    }

    @Override
    public void setUserOtherInfo(UserOtherInfo userOtherInfo) {
        User user = new User();
        user.setId(UserUtils.getUserId());
        user.setEducation(userOtherInfo.getEducation());
        Integer code = (Integer)redisTemplate.opsForValue().get(AuthConstant.SMS_CODE_PREFIX + userOtherInfo.getPhone());
        String phoneCode = String.valueOf(code);
        user.setPhone(StringUtils.hasText(userOtherInfo.getPhoneCode())&&userOtherInfo.getPhoneCode().equals(phoneCode)? userOtherInfo.getPhone() : null);
        String emailCode =  (String) redisTemplate.opsForValue().get(AuthConstant.Email_verification_code + userOtherInfo.getEmail());
        user.setEmail(StringUtils.hasText(userOtherInfo.getEmailCode())&&userOtherInfo.getEmailCode().equals(emailCode)? userOtherInfo.getEmail() : null);
        userMapper.updateById(user);
    }

    @Override
    public HashMap<String, Long> getUserPageBasicInfo(CirclePointsRanking circlePointsRanking) {
        //点赞
        List<Postings> postingsList = postingsMapper.selectList(
                new QueryWrapper<Postings>()
                        .select("like_count")
                        .eq("uid", circlePointsRanking.getUid())
                        .ne("pid", 0)
        );
        int likeNum = postingsList.stream()
                .mapToInt(Postings::getLikeCount)
                .sum();
        //热度
        Double score=redisTemplate.opsForZSet().score(AuthConstant.USER_CIRCLE_RANKING, circlePointsRanking);
        //天数
        Date createTime = this.getOne(new QueryWrapper<User>().eq("id", circlePointsRanking.getUid()).select("create_time")).getCreateTime();
        Date currentDate = new Date(); // 获取当前日期
        long difference = currentDate.getTime() - createTime.getTime(); // 计算相差的时间（毫秒）
        long days = difference / (1000 * 60 * 60 * 24); // 将相差的毫秒数转换为天数

        HashMap<String, Long> userInfo = new HashMap<>();
        userInfo.put("likeNum", (long) likeNum);
        userInfo.put("score", score==null?0:score.longValue());
        userInfo.put("day",days);
        return userInfo;
    }
}
