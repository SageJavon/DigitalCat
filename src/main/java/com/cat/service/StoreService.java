package com.cat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cat.model.PO.Store;
import com.cat.model.DTO.ItemVO;

import java.util.List;
import java.util.Map;

public interface StoreService extends IService<Store> {
    Map<String, List<ItemVO>> getStore();

    void use(Long oldItemId, Long newItemId);

    Map<String, List<ItemVO>> purchase(Long itemId);

    Map<String, List<ItemVO>> feed(Long itemId, Integer restore);

    Map<String, List<ItemVO>> play(Long itemId, Integer restore);
}
