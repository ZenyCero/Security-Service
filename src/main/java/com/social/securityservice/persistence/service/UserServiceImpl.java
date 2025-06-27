package com.social.securityservice.persistence.service;

import com.social.securityservice.domain.jwt.JwtProvider;
import com.social.securityservice.dto.LoginDTO;
import com.social.securityservice.dto.RegisterDTO;
import com.social.securityservice.dto.TokenDTO;
import com.social.securityservice.enums.Roles;
import com.social.securityservice.exception.CustomException;
import com.social.securityservice.persistence.entity.Users;
import com.social.securityservice.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<TokenDTO> login(LoginDTO login) {

        return userRepository
                .findByUsernameOrEmail(login.getUsername(), login.getUsername())
                .filter(user -> passwordEncoder.matches(login.getPassword(), user.getPassword()))
                .map(jwtProvider::createToken)
                .switchIfEmpty(Mono.error(new CustomException("User not found", HttpStatus.BAD_REQUEST)));
    }

    @Override
    public Mono<String> register(RegisterDTO register) {
        Mono<Boolean> existsUser = userRepository
                .findByUsernameOrEmail(register.getUsername(), register.getEmail()).hasElement();

        Users users = Users.builder()
                .username(register.getUsername())
                .email(register.getEmail())
                .password(passwordEncoder.encode(register.getPassword()))
                .roles(Roles.ROLE_USER.name())
                .enabled(true)
                .build();

        return existsUser
                .flatMap(user -> user
                        ? Mono.error(new CustomException("User already exists", HttpStatus.BAD_REQUEST))
                        : userRepository.save(users)
                ).thenReturn("User saved success");

    }

    @Override
    public Mono<Boolean> validate() {
        return Mono.just(true);
    }
}
