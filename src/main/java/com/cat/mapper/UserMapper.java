package com.cat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cat.model.PO.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE open_id=#{openId};")
    User getUserByOpenId(@Param("openId") String openId);

    @Update("UPDATE user SET cat_hungry = IF(cat_state=0, IF(cat_hungry<20, 0, cat_hungry-20), IF(cat_hungry>90, 100, cat_hungry+10))")
    void catHungryChange();

    @Update("UPDATE user SET sign_in = 0")
    void resetSignIn();
}
