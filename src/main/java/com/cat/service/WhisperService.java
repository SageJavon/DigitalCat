package com.cat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cat.model.PO.Whisper;

import java.util.List;

public interface WhisperService extends IService<Whisper> {
    void insert(Long userId, String content);

    List<String> get();

}
