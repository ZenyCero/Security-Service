package com.social.securityservice.persistence.service;

import com.social.securityservice.dto.LoginDTO;
import com.social.securityservice.dto.RegisterDTO;
import com.social.securityservice.dto.TokenDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    public Mono<TokenDTO> login(LoginDTO login);
    public Mono<String> register(RegisterDTO register);
    public Mono<Boolean> validate();
}
