package com.cat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cat.model.PO.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Select("SELECT content FROM message WHERE user_id = #{userId} AND create_time >= #{startDate} ORDER BY create_time DESC")
    List<String> findRecentMessagesByUserId(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);

    @Select("select * from message where user_id = #{userId} order by create_time desc,id desc limit #{limit}")
    List<Message> getDialogue(Long userId, int limit);
}
