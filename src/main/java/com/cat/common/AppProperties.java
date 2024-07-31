package com.cat.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AppProperties {

    @Getter
    private static String apiKey;

    @Value("{app.apiKey}")
    private String key;

    @PostConstruct
    public void init() {
        apiKey = this.key;
    }

}
