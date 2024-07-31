package com.cat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cat.model.PO.Message;

import java.util.ArrayList;
import java.util.List;

public interface MessageService extends IService<Message> {

    ArrayList<Integer> statistics(Integer day);

    List<String> getRecentMessages();

    String getDialogue(Long userId);

    void addMessage(String context, Integer role);

    String chat(Long userId, String userInput);
}
