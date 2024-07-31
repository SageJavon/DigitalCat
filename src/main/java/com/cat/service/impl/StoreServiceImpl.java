package com.cat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cat.common.CommonErrorCode;
import com.cat.common.CommonException;
import com.cat.mapper.StoreMapper;
import com.cat.model.PO.Item;
import com.cat.model.PO.Store;
import com.cat.model.PO.User;
import com.cat.model.DO.ItemDO;
import com.cat.model.DTO.ItemVO;
import com.cat.service.ItemService;
import com.cat.service.StoreService;
import com.cat.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private UserService userService;

    @Resource
    private ItemService itemService;

    @Override
    public Map<String, List<ItemVO>> getStore() {
        Map<String, List<ItemVO>> dic = new HashMap<>();
        List<ItemDO> itemList = storeMapper.getStore(userService.getUser().getId());
        List<ItemVO> foodList = new ArrayList<>();
        List<ItemVO> basinList = new ArrayList<>();
        List<ItemVO> houseList = new ArrayList<>();
        List<ItemVO> nestList = new ArrayList<>();
        List<ItemVO> toyList = new ArrayList<>();
        for (ItemDO item : itemList) {
            item.setStorage(item.getStorage() == null ? 0 : item.getStorage());
            ItemVO newItem = new ItemVO(item);
            switch(item.getType()) {
                case 0:
                    foodList.add(newItem);
                    break;
                case 1:
                    basinList.add(newItem);
                    break;
                case 2:
                    houseList.add(newItem);
                    break;
                case 3:
                    nestList.add(newItem);
                    break;
                case 4:
                    toyList.add(newItem);
                    break;
            }
        }
        dic.put("food", foodList);
        dic.put("basin", basinList);
        dic.put("house", houseList);
        dic.put("nest", nestList);
        dic.put("toy", toyList);
        return dic;
    }

    @Override
    @Transactional
    public void use(Long oldItemId, Long newItemId) {
        Long userId = userService.getUser().getId();
        if(oldItemId == null || newItemId == null) throw new CommonException(CommonErrorCode.ITEM_ID_EMPTY);
        storeMapper.updateUsage(userId, oldItemId, 0);
        storeMapper.updateUsage(userId, newItemId, 1);
    }

    @Override
    @Transactional
    public Map<String, List<ItemVO>> purchase(Long itemId) {
        //获取用户对象
        User user = userService.getUser();
        if(user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        //获取物品对象
        if(itemId == null) throw new CommonException(CommonErrorCode.ITEM_ID_EMPTY);
        Item item = itemService.getById(itemId);
        if(item == null) throw new CommonException(CommonErrorCode.ITEM_NOT_EXIST);
        //查询用户购买记录
        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Store::getUserId, user.getId()).eq(Store::getItemId, itemId);
        Store store = this.getOne(queryWrapper);
        //对于非食物和玩具的物品，如果用户已拥有则不能购买，否则增加猫咪的好感度
        if(item.getType() != 0 && item.getType() != 4) {
            if (store != null && store.getStorage() > 0)
                throw new CommonException(CommonErrorCode.ITEM_ALREADY_OBTAINED);
            user.setFavor(user.getFavor() + item.getRestore());
        }
        //判断用户小鱼干数量是否足够
        int remain = user.getSmallDriedFish() - item.getPrice();
        if(remain < 0)
            throw new CommonException(CommonErrorCode.FISH_NOT_ENOUGH);
        //用户小鱼干减少
        user.setSmallDriedFish(remain);
        userService.updateUser(user);
        //新增记录或添加数量
        if(store == null) {
            Store newStore = Store.builder()
                    .userId(user.getId())
                    .itemId(itemId)
                    .usage(0)
                    .storage(1)
                    .build();
            this.save(newStore);
        }
        else {
            store.setStorage(store.getStorage() + 1);
            this.updateById(store);
        }
        return this.getStore();
    }

    @Override
    @Transactional
    public Map<String, List<ItemVO>> feed(Long itemId, Integer restore) {
        //获取用户对象
        User user = userService.getUser();
        if(user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        //食物数量是否足够
        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Store::getItemId, itemId).eq(Store::getUserId, user.getId());
        Store store = this.getOne(queryWrapper);
        if (store == null || store.getStorage() <= 0) throw new CommonException(CommonErrorCode.FOOD_NOT_ENOUGH);
        //食物数量减
        store.setStorage(store.getStorage() - 1);
        storeMapper.updateById(store);
        //饥饿值加
        int newHunger = user.getCatHungry() + restore;
        newHunger = Math.min(newHunger, 100);
        user.setCatHungry(newHunger);
        userService.updateUser(user);
        return this.getStore();
    }

    @Override
    @Transactional
    public Map<String, List<ItemVO>> play(Long itemId, Integer restore) {
        //获取用户对象
        User user = userService.getUser();
        if(user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        //玩具数量是否足够
        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Store::getItemId, itemId).eq(Store::getUserId, user.getId());
        Store store = this.getOne(queryWrapper);
        if (store == null || store.getStorage() <= 0) throw new CommonException(CommonErrorCode.TOY_NOT_ENOUGH);
        //玩具数量减
        store.setStorage(store.getStorage() - 1);
        storeMapper.updateById(store);
        //好感度加
        int newFavor = user.getFavor() + restore;
        user.setFavor(newFavor);
        userService.updateUser(user);
        return this.getStore();
    }
}
