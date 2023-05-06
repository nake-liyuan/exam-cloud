package com.liyuan.captcha.router;

import com.liyuan.captcha.handler.CaptchaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author liyuan
 * @date 2022/11/20
 * @project exam-cloud
 */
@Configuration
public class CaptchaRouter {

    @Bean
    public RouterFunction<ServerResponse> captchaRouterFunction(CaptchaHandler captchaHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/captcha")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), captchaHandler::handle);
    }

}
