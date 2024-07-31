package com.cat.controller;

import com.cat.annotation.Auth;
import com.cat.common.Result;
import com.cat.model.DTO.SessionData;
import com.cat.service.UserService;
import com.cat.utils.SessionUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.cat.common.CommonException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/profile")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private SessionUtils sessionUtils;

    @PostMapping("/login/{code}")
    public Result<SessionData> login(@NotBlank @PathVariable String code) {
        try {
            return Result.success(userService.login(code));
        }catch (CommonException e){
            return  Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping
    public Result<SessionData> getUserSessionData(){
        try{
            return Result.success(sessionUtils.getSessionData());
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @GetMapping("/check")
    public Result<String> checkSession(){
        try {
            if(sessionUtils.getSessionData() == null) return Result.fail("登录失效");
            return Result.success("已登录");
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PutMapping
    public Result<String> updateInfo(@RequestBody SessionData sessionData){
        try {
            userService.updateInfo(sessionData);
            return Result.success("修改成功");
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile image) {
        try {
            return Result.success(userService.upload(image));
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        } catch (IOException e) {
            return Result.fail("上传失败");
        }
    }

    @Auth
    @PostMapping("/signIn")
    public Result<String> signIn() {
        try {
            return Result.success(userService.signIn());
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
