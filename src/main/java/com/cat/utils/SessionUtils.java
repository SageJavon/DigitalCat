package com.cat.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import com.cat.common.CommonConstants;
import com.cat.model.DTO.SessionData;
import com.cat.model.PO.User;
import com.cat.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionUtils {

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    @Resource
    private RedisUtils redisUtil;

    @Resource
    private UserMapper userMapper;

    //获取用户id
    public Long getUserId(){
        return Optional
                .ofNullable(getSessionData())
                .orElse(new SessionData())
                .getId();
    }

    //获取会话实体
    public SessionData getSessionData(){
        String key = request.getHeader(CommonConstants.SESSION);
        if(key == null) return null;
        SessionData sessionData;
        try {
            sessionData = (SessionData) redisUtil.get(key);
        }catch (Exception e){
            return getSessionDataFromDB(key);
        }
        if(sessionData != null) return sessionData;
        return getSessionDataFromDB(key);
    }

    public String getSessionId() {
        return request.getHeader(CommonConstants.SESSION);
    }

    public void setSessionId(String sessionId){
        response.setHeader(CommonConstants.SESSION, sessionId);
    }

    //随机生成sessionId
    public String generateSessionId(){
        String sessionId = UUID.randomUUID().toString();
        response.setHeader(CommonConstants.SESSION, sessionId);
        return sessionId;
    }

    public void invalidate(){
        request.removeAttribute(CommonConstants.SESSION);
    }

    // 从数据库获取user对象并将对应的sessionData缓存在redis中
    private SessionData getSessionDataFromDB(String key) {
        SessionData sessionData;
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("session_id", key);
        User user = userMapper.selectOne(userQueryWrapper);
        if(user != null){
            sessionData = new SessionData(user);
            redisUtil.set(key, sessionData, 600);
            return sessionData;
        }else{
            redisUtil.set(key,null,30);
            return null;
        }
    }

    //写user对象，同步redis和mysql
    @Transactional
    public void setSessionData(User user){
        String key = request.getHeader(CommonConstants.SESSION);
        if(key == null) return;
        try {
            redisUtil.set(key, new SessionData(user));
        } catch (Exception e) {
            userMapper.updateById(user);
        }
        userMapper.updateById(user);
    }

    public void deleteSessionData(){
        String key = request.getHeader(CommonConstants.SESSION);
        if(key == null) return;
        redisUtil.del(key);
    }
}
