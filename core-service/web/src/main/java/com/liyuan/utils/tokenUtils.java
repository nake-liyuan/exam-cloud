package com.liyuan.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;

import java.text.ParseException;

/**
 * @author liyuan
 * @date 2023/4/2
 * @project exam-cloud
 */
public class tokenUtils {

    public static JSONObject payload(String token) throws ParseException {
        return JSONUtil.parseObj(JWSObject.parse(token).getPayload().toString());

    }

    public static String expirationDetection(String token) throws ParseException {
        JSONObject payload = JSONUtil.parseObj(JWSObject.parse(token).getPayload().toString());
        // JWT过期时间戳(单位：秒)
        Long expireTime = payload.getLong("exp");
        // 当前时间（单位：秒）
        long currentTime = System.currentTimeMillis() / 1000;
        // token未过期
        if (expireTime > currentTime) {
            return "激活";
        }else {
            return "过期";
        }
    }

    public static String jti(String token) throws ParseException {
        JSONObject payload = JSONUtil.parseObj(JWSObject.parse(token).getPayload().toString());
        return payload.getStr("jti");
    }
}
