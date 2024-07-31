package com.cat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cat.model.PO.DailySentence;

public interface DailySentenceService extends IService<DailySentence> {
    String getSentence();
}
