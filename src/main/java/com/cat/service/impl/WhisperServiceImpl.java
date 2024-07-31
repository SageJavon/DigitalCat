package com.cat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cat.common.CommonErrorCode;
import com.cat.common.CommonException;
import com.cat.model.PO.Whisper;
import com.cat.mapper.WhisperMapper;
import com.cat.service.WhisperService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WhisperServiceImpl extends ServiceImpl<WhisperMapper, Whisper> implements WhisperService {

    @Resource
    private WhisperMapper whisperMapper;

    @Override
    public void insert(Long userId, String content) {
        if(userId == null) throw new CommonException(CommonErrorCode.USER_ID_EMPTY);
        if(content == null || content.isBlank()) throw new CommonException(CommonErrorCode.CONTENT_EMPTY);
        Whisper whisper = Whisper.builder().userId(userId).content(content).build();
        this.baseMapper.insert(whisper);
    }

    @Override
    public List<String> get() {
        return whisperMapper.get();
    }

}
