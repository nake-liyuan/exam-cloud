package com.liyuan.captcha.handler;

import cn.hutool.core.util.IdUtil;
import com.liyuan.api.Result;
import com.liyuan.captcha.enums.CaptchaTypeEnum;
import com.liyuan.captcha.producer.CaptchaProducer;
import com.liyuan.constant.AuthConstant;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证码处理器
 * @author liyuan
 * @date 2022/11/20
 * @project exam-cloud
 */
@Component
@RequiredArgsConstructor
public class CaptchaHandler implements HandlerFunction<ServerResponse> {

    private final CaptchaProducer captchaProducer;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 验证码类型，默认:ARITHMETIC
     */
    @Value("${captcha.type:ARITHMETIC}")
    CaptchaTypeEnum captchaType;

    /**
     * 验证码值的有效期(单位:秒)，默认:120
     */
    @Value("${captcha.ttl:120}")
    long captchaValueTtl ;


    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        Captcha captcha = captchaProducer.getCaptcha(captchaType);
        String captchaValue = captcha.text();

        // 对于数学类型的需要进行处理
        if (captchaType == null || captchaType == CaptchaTypeEnum.ARITHMETIC) {
            if (captcha.getCharType() - 1 == CaptchaTypeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
                captchaValue = captchaValue.split("\\.")[0];
            }
        }

        // 缓存验证码至Redis
        String uuid = IdUtil.simpleUUID();
        stringRedisTemplate.opsForValue().set(AuthConstant.VALIDATION_CODE_KEY_PREFIX + uuid, captchaValue, captchaValueTtl, TimeUnit.SECONDS);

        // 获取到验证码Base64编码字符串
        String captchaBase64 = captcha.toBase64();
        Map<String, String> result = new HashMap<>(2);
        result.put("uuid", uuid);
        result.put("img", captchaBase64);

        return ServerResponse.ok().body(BodyInserters.fromValue(Result.success(result)));
    }
}
