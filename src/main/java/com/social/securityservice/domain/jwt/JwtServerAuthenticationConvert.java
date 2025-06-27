package com.social.securityservice.domain.jwt;

import com.social.securityservice.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtServerAuthenticationConvert implements ServerAuthenticationConverter {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().toString();

        if (!path.contains("auth/validate") && path.contains("auth")) {
            System.out.println("JwtServerAuthenticationConvert - primer if");
            return Mono.empty();
        }
        System.out.println("JwtServerAuthenticationConvert - try to aouth");
        String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth == null)
            return Mono.error(new CustomException("Token wasn't found", HttpStatus.BAD_REQUEST));

        if(!auth.startsWith("Bearer "))
            return Mono.error(new CustomException("Invalid token", HttpStatus.BAD_REQUEST));

        String token = auth.replace("Bearer ", "");
        jwtProvider.validateToken(token);

        exchange.getAttributes().put("token", token);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(null, token);

        return Mono.just(authenticationToken);
    }
}
