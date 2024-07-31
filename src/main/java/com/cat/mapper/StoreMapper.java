package com.cat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cat.model.PO.Store;
import com.cat.model.DO.ItemDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StoreMapper extends BaseMapper<Store> {
    @Select("select i.id, type, name, img, price, restore, storage, `desc`, `usage` " +
            "from item i left join (select * from store where user_id=#{userId}) s on i.id = item_id order by i.id")
    List<ItemDO> getStore(Long userId);

    @Update("update store set `usage`=#{usage} where user_id=#{userId} and item_id=#{itemId}")
    void updateUsage(Long userId, Long itemId, int usage);
}
