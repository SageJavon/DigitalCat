package com.cat.controller;

import com.cat.annotation.Auth;
import com.cat.common.CommonException;
import com.cat.common.Result;
import com.cat.model.DTO.ItemVO;
import com.cat.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ItemController {

    @Resource
    private StoreService storeService;

    @Auth
    @GetMapping("/itemList")
    public Result<Map<String, List<ItemVO>>> getStore() {
        try {
            return Result.success(storeService.getStore());
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping("/use")
    public Result<Void> use(Long oldItemId, Long newItemId) {
        try {
            storeService.use(oldItemId, newItemId);
            return Result.success(null);
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping("/buy")
    public Result<Map<String, List<ItemVO>>> purchase(Long itemId) {
        try {
            return Result.success(storeService.purchase(itemId));
        } catch (CommonException e) {
            return Result.result(e.getCommonErrorCode());
        }
    }
}
