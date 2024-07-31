package com.cat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cat.model.PO.DailySentence;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;


@Mapper
public interface DailySentenceMapper extends BaseMapper<DailySentence> {

    @Select("SELECT content FROM daily_sentence ORDER BY RAND() LIMIT 1")
    String getSentence();
}
