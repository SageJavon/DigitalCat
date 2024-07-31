package com.cat.controller;

import com.cat.annotation.Auth;
import com.cat.common.CommonException;
import com.cat.common.Result;
import com.cat.service.WhisperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/whisper")
public class WhisperController {

    @Resource
    WhisperService whisperService;

    @Auth
    @PostMapping
    public Result<String> insert(Long userId, String content) {
        try{
            whisperService.insert(userId, content);
            return Result.success("保存悄悄话成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping
    public Result<List<String>> get() {
        try {
            return Result.success(whisperService.get());
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

}
