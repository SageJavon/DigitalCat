package com.cat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cat.model.PO.DailySentence;
import com.cat.mapper.DailySentenceMapper;
import com.cat.service.DailySentenceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DailySentenceServiceImpl extends ServiceImpl<DailySentenceMapper, DailySentence> implements DailySentenceService {

    @Resource
    DailySentenceMapper dailySentenceMapper;

    @Override
    public String getSentence() {
        return dailySentenceMapper.getSentence();
    }
}
