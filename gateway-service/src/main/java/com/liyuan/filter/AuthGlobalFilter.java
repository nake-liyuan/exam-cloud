package com.liyuan.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.liyuan.api.Result;
import com.liyuan.constant.AuthConstant;
import com.nimbusds.jose.JWSObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.text.ParseException;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 * @author liyuan
 * @date 2022/10/15
 * @project exam-cloud
 */

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static Logger LOGGER = LoggerFactory.getLogger(AuthGlobalFilter.class);

    private final RedisTemplate redisTemplate;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 拦截黑名单的JWT
        String token = exchange.getRequest().getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
        if (StrUtil.isEmpty(token)) {
            return chain.filter(exchange);
        }

        String payload = this.getPayload(token);
        if (StrUtil.isBlank(payload)) {
            return chain.filter(exchange);
        }

        String jti = JSONUtil.parseObj(payload).getStr("jti");
        Boolean isBlackJwt = redisTemplate.hasKey(AuthConstant.TOKEN_BLACKLIST_PREFIX + jti);
        if (isBlackJwt) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.getHeaders().set("Access-Control-Allow-Origin", "*");
            response.getHeaders().set("Cache-Control", "no-cache");
            String body = JSONUtil.toJsonStr(Result.unauthorized("Not Authenticated"));
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
            return response.writeWith(Mono.just(buffer));
        }

        //从token中解析用户信息并设置到Header中去
        String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        JWSObject jwsObject = JWSObject.parse(realToken);
        String userStr = jwsObject.getPayload().toString();
        LOGGER.info("AuthGlobalFilter.filter() user:{}",userStr);
        ServerHttpRequest request = exchange.getRequest().mutate().header(AuthConstant.USER_TOKEN_HEADER, userStr).build();
        exchange = exchange.mutate().request(request).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public String getPayload(String authorization) throws ParseException {
        String payload = null;
        if (StrUtil.isNotBlank(authorization) && StrUtil.startWithIgnoreCase(authorization, "Bearer ")) {
            authorization = StrUtil.replaceIgnoreCase(authorization, "Bearer ", "");
            payload = JWSObject.parse(authorization).getPayload().toString();
        }
        return payload;
    }
}
