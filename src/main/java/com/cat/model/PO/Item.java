package com.cat.model.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //0:food, 1:basin, 2:house, 3:nest, 4:toy
    private Integer type;

    private String name;

    private String img;

    private Integer price;

    private Integer restore;

    @TableField(value = "`desc`")
    private String desc;
}
