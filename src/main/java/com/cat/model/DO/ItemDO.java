package com.cat.model.DO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDO {
    private Long id;

    private Integer type;

    private String name;

    private String img;

    private Integer price;

    private Integer restore;

    private Integer storage;

    private String desc;

    private Integer usage;
}
