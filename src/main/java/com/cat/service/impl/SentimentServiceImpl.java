package com.cat.service.impl;

import com.cat.service.SentimentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import javax.annotation.Resource;

@Service
@Slf4j
public class SentimentServiceImpl implements SentimentService {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public String analysis(String context){
        String url = "https://89e265v638.vicp.fun:443/sentiments";
        String encodedContext;
        encodedContext = URLEncoder.encode(context, StandardCharsets.UTF_8);
        URI targetUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("text", encodedContext)
                .build()
                .toUri();
        log.info(targetUrl.toString());
        String response = restTemplate.getForObject(targetUrl, String.class);
        if (response == null) return null;
        JSONObject jsonResponse = new JSONObject(response);
        log.info(jsonResponse.toString());
        return jsonResponse.getString("data");
    }
}
