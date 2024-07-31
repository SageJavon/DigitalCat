package com.cat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cat.chatglm.ChatClient;
import com.cat.chatglm.ConstantValue;
import com.cat.common.CommonErrorCode;
import com.cat.common.CommonException;
import com.cat.model.PO.Message;
import com.cat.mapper.MessageMapper;
import com.cat.service.MessageService;
import com.cat.service.SentimentService;
import com.cat.service.UserService;
import com.cat.utils.ChatGLMUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private SentimentService sentimentService;

    @Resource
    private UserService userService;

    @Override
    public ArrayList<Integer> statistics(Integer day) {
        Long userId = userService.getUser().getId();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime queryTime = now.minusDays(day);
        LambdaQueryWrapper<Message> queryWrapperTotal = new LambdaQueryWrapper<>();
        queryWrapperTotal.ge(Message::getCreateTime, queryTime);
        queryWrapperTotal.eq(Message::getUserId, userId);
        queryWrapperTotal.eq(Message::getRole, 0);
        Integer total = this.count(queryWrapperTotal);

        LambdaQueryWrapper<Message> queryWrapperHappiness = new LambdaQueryWrapper<>();
        queryWrapperHappiness.eq(Message::getUserId, userId);
        queryWrapperHappiness.ge(Message::getCreateTime, queryTime);
        queryWrapperHappiness.eq(Message::getSentiment, "happiness");
        queryWrapperHappiness.eq(Message::getRole, 0);
        Integer happiness = this.count(queryWrapperHappiness);

        LambdaQueryWrapper<Message> queryWrapperSadness = new LambdaQueryWrapper<>();
        queryWrapperSadness.eq(Message::getUserId, userId);
        queryWrapperSadness.ge(Message::getCreateTime, queryTime);
        queryWrapperSadness.eq(Message::getSentiment, "sadness");
        queryWrapperSadness.eq(Message::getRole, 0);
        Integer sadness = this.count(queryWrapperSadness);

        LambdaQueryWrapper<Message> queryWrapperLike = new LambdaQueryWrapper<>();
        queryWrapperLike.eq(Message::getUserId, userId);
        queryWrapperLike.ge(Message::getCreateTime, queryTime);
        queryWrapperLike.eq(Message::getSentiment, "like");
        queryWrapperLike.eq(Message::getRole, 0);
        Integer like = this.count(queryWrapperLike);

        LambdaQueryWrapper<Message> queryWrapperFear = new LambdaQueryWrapper<>();
        queryWrapperFear.eq(Message::getUserId, userId);
        queryWrapperFear.ge(Message::getCreateTime, queryTime);
        queryWrapperFear.eq(Message::getSentiment, "fear");
        queryWrapperFear.eq(Message::getRole, 0);
        Integer fear = this.count(queryWrapperFear);

        LambdaQueryWrapper<Message> queryWrapperDisgust = new LambdaQueryWrapper<>();
        queryWrapperDisgust.eq(Message::getUserId, userId);
        queryWrapperDisgust.ge(Message::getCreateTime, queryTime);
        queryWrapperDisgust.eq(Message::getSentiment, "disgust");
        queryWrapperDisgust.eq(Message::getRole, 0);
        Integer disgust = this.count(queryWrapperDisgust);

        LambdaQueryWrapper<Message> queryWrapperSurprise = new LambdaQueryWrapper<>();
        queryWrapperSurprise.eq(Message::getUserId, userId);
        queryWrapperSurprise.ge(Message::getCreateTime, queryTime);
        queryWrapperSurprise.eq(Message::getSentiment, "surprise");
        queryWrapperSurprise.eq(Message::getRole, 0);
        Integer surprise = this.count(queryWrapperSurprise);

        LambdaQueryWrapper<Message> queryWrapperAnger = new LambdaQueryWrapper<>();
        queryWrapperAnger.eq(Message::getUserId, userId);
        queryWrapperAnger.ge(Message::getCreateTime, queryTime);
        queryWrapperAnger.eq(Message::getSentiment, "anger");
        queryWrapperAnger.eq(Message::getRole, 0);
        Integer anger = this.count(queryWrapperAnger);

        ArrayList<Integer> result = new ArrayList<>();
        result.add(total);
        result.add(happiness);
        result.add(sadness);
        result.add(like);
        result.add(fear);
        result.add(disgust);
        result.add(surprise);
        result.add(anger);
        return result;
    }

    //获取一周的用户文本
    public List<String> getRecentMessages() {
        Long userId = userService.getUser().getId();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusWeeks(1);
        return messageMapper.findRecentMessagesByUserId(userId, startDate);
    }

    //获取8对用户对话数据,转换为json格式
    @Override
    public String getDialogue(Long userId) {
        StringBuilder history = new StringBuilder();
        List<Message> messageList = messageMapper.getDialogue(userId, 8*2);
        Collections.reverse(messageList);
        for(Message message : messageList) {
            String role;
            if(message.getRole() == 0)
                role = ConstantValue.user_role;
            else
                role = ConstantValue.assistant_role;
            history.append(ChatGLMUtils.createJson(role, message.getContent()));
            history.append(",");
        }
        return String.valueOf(history);
    }

    @Override
    public void addMessage(String context, Integer role) {
        Message message = Message.builder().content(context).role(role).userId(userService.getUser().getId()).build();
        //用户情感分析
        if(role == 0) {
            String label = sentimentService.analysis(context);
            message.setSentiment(label);
        }
        messageMapper.insert(message);
    }

    @Override
    @Transactional
    public String chat(Long userId, String userInput) {
        String apiKey = ChatGLMUtils.loadApiKey();
        if (apiKey == null)
            throw new CommonException(CommonErrorCode.API_KEY_NOT_FOUND);
        //构造历史数据列表
        String history = this.getDialogue(userId);
        ChatClient chat = new ChatClient(apiKey);
        chat.AsyncInvoke(userInput, true, history);
        String output = chat.getResponseMessage();
        //保存对话数据
        addMessage(userInput, 0);
        addMessage(output, 1);
        return output;
    }
}
