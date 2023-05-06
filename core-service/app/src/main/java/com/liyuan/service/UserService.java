package com.liyuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.domain.User;
import com.liyuan.domain.app.vo.postings.CirclePointsRanking;
import com.liyuan.entity.dto.UserOtherInfo;

import java.util.HashMap;

/**
* @author liyuan
*@date 2022/10/15
*@project exam-cloud
*/
public interface UserService extends IService<User>{

    User getUserInfo();

    User getuserOtherInfo();

    void setUserOtherInfo(UserOtherInfo userOtherInfo);
    HashMap<String,Long> getUserPageBasicInfo(CirclePointsRanking circlePointsRanking);
}
