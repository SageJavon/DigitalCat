package com.cat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cat.model.PO.Whisper;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface WhisperMapper extends BaseMapper<Whisper> {

    @Select("SELECT content FROM whisper ORDER BY RAND() LIMIT 10")
    List<String> get();

}
