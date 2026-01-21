package com.qmeetx.apigateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GatewayExceptionHandler {


    @ExceptionHandler(jwtMissingException.class)
    public Mono<Void> handleJwtMissingException(ServerWebExchange exchange, jwtMissingException ex)
    {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(("{\"error\":\"" + ex.getMessage() + "\"}").getBytes())));
    }


    @ExceptionHandler(JwtInvalidException.class)
    public Mono<Void> handleJwtInvalidException(ServerWebExchange exchange,JwtInvalidException jwtInvalidException) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(("{\"error\":\"" + jwtInvalidException.getMessage() + "\"}").getBytes())
                ));

    }
    @ExceptionHandler(Exception.class)
    public Mono<Void> handleGenericException(ServerWebExchange exchange, Exception ex) {
        ex.printStackTrace(); // Critical for debugging
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(("{\"error\":\"Internal server error: " + ex.getMessage() + "\"}").getBytes())));
    }




    }






