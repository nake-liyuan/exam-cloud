package com.liyuan.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.liyuan.constant.AuthConstant;
import com.liyuan.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
@SuppressWarnings({"unchecked","rawtypes"})
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean sendSms( String phone) {
        if(StringUtils.isEmpty(phone)) {
            return false;
        }
        Config config = new Config()
                .setAccessKeyId("LTAI5tPRFiDJmzu21wQqajzX")
                .setAccessKeySecret("wjEO3l67eVNBiGna4r68bOcjOya0da");
        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client = null;
        String code = RandomUtil.randomNumbers(6); // 随机生成6位的验证码
        Map map = new HashMap();
        map.put("code",code);
        try {
            client = new Client(config);
            SendSmsRequest request = new SendSmsRequest();
            request.setSignName("阿里云短信测试");//签名名称
            request.setTemplateCode("SMS_154950909");//模版Code
            request.setPhoneNumbers("17585419815");//电话号码
            //这里的参数是json格式的字符串
            request.setTemplateParam(JSONObject.toJSONString(map));
            SendSmsResponse response = client.sendSms(request);
            stringRedisTemplate.opsForValue().set(AuthConstant.SMS_CODE_PREFIX + phone, code, 600, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
