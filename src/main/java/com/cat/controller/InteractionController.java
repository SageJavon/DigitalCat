package com.cat.controller;

import com.cat.annotation.Auth;
import com.cat.common.CommonException;
import com.cat.common.Result;
import com.cat.model.DTO.ItemVO;
import com.cat.service.StoreService;
import com.cat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/interaction")
public class InteractionController {

    @Resource
    private UserService userService;

    @Resource
    private StoreService storeService;

    @Auth
    @PostMapping("/sleepOrWake")
    public Result<Void> changeCatState(Integer state) {
        try {
            userService.changeCatState(state);
           return Result.success(null);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping("/stroke")
    public Result<Integer> stroke() {
        try {
            return Result.success(userService.stroke());
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping("/feed")
    public Result<Map<String, List<ItemVO>>> feed(Long itemId, Integer restore) {
        try {
            return Result.success(storeService.feed(itemId, restore));
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping("/play")
    public Result<Map<String, List<ItemVO>>> play(Long itemId, Integer restore) {
        try {
            return Result.success(storeService.play(itemId, restore));
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

}
