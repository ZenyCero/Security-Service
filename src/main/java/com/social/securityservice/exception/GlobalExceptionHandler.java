package com.social.securityservice.exception;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        String message = ex.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof CustomException customException){
            message = customException.getMessage();
            status = customException.getStatus();
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);

        DataBuffer dataBuffer = exchange.getResponse()
                .bufferFactory()
                .wrap(message.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
