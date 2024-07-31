package com.cat.model.DTO;

import com.cat.model.DO.ItemDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemVO {
    private Long id;

    private String name;

    private String img;

    private Integer price;

    private Integer restore;

    private Integer storage;

    private String desc;

    private Integer usage;

    public ItemVO(ItemDO item) {
        this.id = item.getId();
        this.name = item.getName();
        this.img = item.getImg();
        this.price = item.getPrice();
        this.restore = item.getRestore();
        this.storage = item.getStorage();
        this.desc = item.getDesc();
        this.usage = item.getUsage();
    }
}
