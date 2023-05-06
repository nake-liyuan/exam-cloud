package com.liyuan.util;

import cn.hutool.json.JSONObject;
import com.liyuan.constant.AuthConstant;

/**
 * @author liyuan
 * @date 2022/11/22
 * @project exam-cloud
 */
public class UserUtils {
    /**
     * 获取当前登录用户的ID
     *
     * @return
     */
    public static String getUserId() {
        String id = null;
        JSONObject jwtPayload = JwtUtils.getJwtPayload();
        if (jwtPayload != null) {
            id = jwtPayload.getStr("id");
        }
        return id;
    }

    /**
     * 解析JWT获取获取用户名
     *
     * @return
     */
    public static String getUsername() {
        String username = JwtUtils.getJwtPayload().getStr(AuthConstant.USER_NAME_KEY);
        return username;
    }

    /**
     * 解析JWT获取获取用户角色authorities
     *
     * @return
     */
    public static String getAuthorities() {
        String roleInfo = JwtUtils.getJwtPayload().getStr(AuthConstant.AUTHORITY_CLAIM_NAME);
        return roleInfo;
    }
}
