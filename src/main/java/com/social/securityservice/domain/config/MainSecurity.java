package com.social.securityservice.domain.config;

import com.social.securityservice.domain.jwt.JwtAuthenticationManager;
import com.social.securityservice.domain.jwt.JwtServerAuthenticationConvert;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

@Configuration
@EnableWebFluxSecurity
public class MainSecurity {

    @Bean
    public SecurityWebFilterChain chain(ServerHttpSecurity http,
                                        JwtAuthenticationManager authenticationManager,
                                        ServerAuthenticationConverter convert){

        AuthenticationWebFilter authFilter =
                new AuthenticationWebFilter(authenticationManager);
        authFilter.setServerAuthenticationConverter(convert);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/auth/validate").authenticated()
                        .pathMatchers("/auth/**").permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
