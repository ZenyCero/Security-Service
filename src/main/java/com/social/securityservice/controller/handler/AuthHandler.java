package com.social.securityservice.controller.handler;

import com.social.securityservice.dto.LoginDTO;
import com.social.securityservice.dto.RegisterDTO;
import com.social.securityservice.dto.TokenDTO;
import com.social.securityservice.persistence.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final UserService userService;

    public Mono<ServerResponse> validate(ServerRequest request){

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.validate(), Boolean.class);
    }

    public Mono<ServerResponse> login(ServerRequest request){
        System.out.println("controller login");
        Mono<LoginDTO> loginDTO = request.bodyToMono(LoginDTO.class);
        return loginDTO.flatMap(login -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.login(login), TokenDTO.class));
    }

    public Mono<ServerResponse> register(ServerRequest request){
        Mono<RegisterDTO> registerDTO = request.bodyToMono(RegisterDTO.class);
        return registerDTO.flatMap(register -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.register(register), String.class));
    }
}
