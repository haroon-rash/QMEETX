package com.qmeetx.apigateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class jwtProperties {


    private List<String> publicPaths = new ArrayList<>();

}
