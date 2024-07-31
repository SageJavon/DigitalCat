package com.cat.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

import static org.springframework.util.unit.DataSize.of;

@Configuration
public class MultipartConfig {
    //设置上传文件大小限制
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory config = new MultipartConfigFactory();
        config.setMaxFileSize(of(1024, DataUnit.MEGABYTES));
        config.setMaxRequestSize(of(1024, DataUnit.MEGABYTES));
        return config.createMultipartConfig();
    }
}
