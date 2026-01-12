/*
package com.qmeetx.apigateway.filter;

import com.qmeetx.apigateway.exceptions.JwtInvalidException;
import com.qmeetx.apigateway.exceptions.jwtMissingException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {


    private final WebClient webClient;
    private final String headerName;
    private final String authServiceUrl;
    private final String prfix;
    private final Duration timeout;
    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder , Environment env) {
        this.prfix=env.getProperty("jwt.prefix");
        this.headerName=env.getProperty("jwt.header");
        this.authServiceUrl=env.getProperty("jwt.auth-service-validate-url");
        this.timeout=Duration.ofSeconds(Long.parseLong(env.getProperty("jwt.timeout","5").replaceAll("s","")));


        this.webClient=webClientBuilder.baseUrl(authServiceUrl).build();


    }

    @Override
    public GatewayFilter apply(Object config) {

        return (exchange, chain) ->

        {

            String token =exchange.getRequest().getHeaders().getFirst(headerName);
            if(token==null || !token.startsWith(prfix)) {
                throw new jwtMissingException("Authorization header is missing or invalid");
            }

        return webClient.get()
                .uri("/validate")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .toBodilessEntity()
                .timeout(timeout)
                .onErrorMap(WebClientResponseException.Unauthorized.class,
                        ex -> new JwtInvalidException("JWT token is invalid"))
                .flatMap(response -> chain.filter(exchange));



        };
    }
}
*/
