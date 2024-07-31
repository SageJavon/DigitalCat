package com.cat.controller;

import com.cat.annotation.Auth;
import com.cat.common.Result;
import com.cat.service.MessageService;
import com.cat.utils.ChatGLMUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/psychology")
public class PsychologyController {

    @Resource
    private MessageService messageService;

    @Resource
    private RestTemplate restTemplate;

    @Auth
    @GetMapping("/advice")
    public Result<String> getAdvice(){
        List<String> messageList = messageService.getRecentMessages();
        if(messageList == null || messageList.isEmpty()) return Result.success("最近没有对话数据，心理建议为空。");
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < messageList.size(); i++) {
            String message = messageList.get(i);
            String processedMessage = "用户文本" + (i + 1) + "：" + message + "\n";
            s.append(processedMessage);
        }
        String question = ChatGLMUtils.sendRequest(String.valueOf(s));
        if (question.equals("没问题") || question.isBlank())
            return Result.success("你最近没有任何问题噢，继续愉快地与猫咪玩耍吧！");
        log.info("问题如下: {}", question);

        String url = "https://89e265v638.vicp.fun:443/qwen";
        String encodedQuestion;
        encodedQuestion = URLEncoder.encode(question, StandardCharsets.UTF_8);
        URI targetUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("content", encodedQuestion)
                .build()
                .toUri();
        String response = restTemplate.getForObject(targetUrl, String.class);
        if (response == null || response.isBlank()) return Result.success("大模型正在处理其他消息中，请等待...");
        JSONObject jsonResponse = new JSONObject(response);
        response = jsonResponse.getString("answer");
        return Result.success(response);
    }

    @Auth
    @GetMapping("/statistics")
    public Result<Map<String, Float>> getStatistics(Integer day){
        ArrayList<Integer> count;
        count = messageService.statistics(day);
        log.info("情感分析统计为统计个为 {}",count);
        Map<String, Float> res = new HashMap<>();
        Integer cnt1=count.get(1);
        Integer cnt2=count.get(2);
        Integer cnt3=count.get(3);
        Integer cnt4=count.get(4);
        Integer cnt5=count.get(5);
        Integer cnt6=count.get(6);
        Integer cnt7=count.get(7);
        int sum=cnt1+cnt2+cnt3+cnt4+cnt5+cnt6+cnt7;
        res.put("开心", sum == 0 ? 0 : cnt1.floatValue()/sum);
        res.put("伤心", sum == 0 ? 0 : cnt2.floatValue()/sum);
        res.put("好奇", sum == 0 ? 0 : cnt3.floatValue()/sum);
        res.put("恐惧", sum == 0 ? 0 : cnt4.floatValue()/sum);
        res.put("厌恶", sum == 0 ? 0 : cnt5.floatValue()/sum);
        res.put("惊喜", sum == 0 ? 0 : cnt6.floatValue()/sum);
        res.put("生气", sum == 0 ? 0 : cnt7.floatValue()/sum);
        return Result.success(res);
    }
}
