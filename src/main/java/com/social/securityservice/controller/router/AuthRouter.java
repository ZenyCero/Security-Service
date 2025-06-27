package com.social.securityservice.controller.router;

import com.social.securityservice.controller.handler.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AuthRouter {

    private static final String PATH = "/auth";

    @Bean
    public RouterFunction<ServerResponse> auth(AuthHandler handler){
        return RouterFunctions.route()
                .GET(PATH + "/validate", handler::validate)
                .POST(PATH + "/login", handler::login)
                .POST(PATH + "/register", handler::register)
                .build();
    }
}
