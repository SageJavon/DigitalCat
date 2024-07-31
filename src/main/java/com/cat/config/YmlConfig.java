package com.cat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class YmlConfig {

    @Value("${server.port}")
    private String port;

    @Value("${mini-app.app-id}")
    private String appId;

    @Value("${mini-app.app-secret}")
    private String appSecret;

}
