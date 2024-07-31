package com.cat.controller;

import com.cat.annotation.Auth;
import com.cat.common.CommonException;
import com.cat.common.Result;
import com.cat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private MessageService messageService;

    @Auth
    @PostMapping("/message")
    public Result<String> addMessage(String content) {
        try {
            messageService.addMessage(content, 0);
            return Result.success("添加消息成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping("/chat")
    public Result<String> chat(Long userId, String userInput) {
        try {
            log.info("用户发送消息：{}", userInput);
            String output = messageService.chat(userId, userInput);
            log.info("GLM回复：{}", output);
            return Result.success(output);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
