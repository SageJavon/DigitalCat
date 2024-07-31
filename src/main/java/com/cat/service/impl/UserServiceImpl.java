package com.cat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cat.common.CommonConstants;
import com.cat.common.CommonErrorCode;
import com.cat.common.CommonException;
import com.cat.config.YmlConfig;
import com.cat.model.DTO.SessionData;
import com.cat.model.DTO.WxSession;
import com.cat.model.PO.Item;
import com.cat.model.PO.Store;
import com.cat.model.PO.User;
import com.cat.mapper.UserMapper;
import com.cat.service.DailySentenceService;
import com.cat.service.StoreService;
import com.cat.service.UserService;
import com.cat.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SessionUtils sessionUtils;

    @Resource
    private StoreService storeService;

    @Resource
    private YmlConfig ymlConfig;

    @Resource
    private AliossUtils aliossUtils;

    @Resource
    private DailySentenceService dailySentenceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SessionData login(String code) {
        //shadow test
        if (CommonConstants.SHADOW_TEST.equals(code)) {
            //设置响应头
            sessionUtils.setSessionId(CommonConstants.SHADOW_TEST);
            return new SessionData();
        }
        WxSession wxSession = Optional.ofNullable(getWxSessionByCode(code))
                .orElse(new WxSession());
        checkWxSession(wxSession);
        //根据openid查询DB
        User user = userMapper.getUserByOpenId(wxSession.getOpenId());
        //如果用户已注册，则返回用户
        if (user != null) {
            sessionUtils.setSessionId(user.getSessionId());
            return new SessionData(user);
        }
        //如果未注册，则将用户信息存入DB
        //生成sessionId
        String sessionId = sessionUtils.generateSessionId();
        User newUser = User.builder()
                .sessionId(sessionId)
                .openId(wxSession.getOpenId())
                .unionId(wxSession.getUnionId())
                .sessionKey(wxSession.getSessionKey())
                .nickname("小猫咪")
                .avatar(CommonConstants.DEFAULT_AVATAR)
                .gender(0)
                .smallDriedFish(100)
                .catHungry(100)
                .signIn(0)
                .catState(0)
                .favor(0)
                .build();
        userMapper.insert(newUser);
        //添加默认物品记录
        List<Store> batchList = new ArrayList<>();
        Store item1 = Store.builder()
                .userId(newUser.getId())
                .itemId(7L)
                .usage(1)
                .storage(1)
                .build();
        batchList.add(item1);
        Store item2 = Store.builder()
                .userId(newUser.getId())
                .itemId(12L)
                .usage(1)
                .storage(1)
                .build();
        batchList.add(item2);
        Store item3 = Store.builder()
                .userId(newUser.getId())
                .itemId(15L)
                .usage(1)
                .storage(1)
                .build();
        batchList.add(item3);
        storeService.saveBatch(batchList);
        return new SessionData(newUser);
    }

    //使用 code 换取 openid、session_key、unionId 等信息
    @Override
    public WxSession getWxSessionByCode(String code) {
        Map<String, String> requestUrlParam = new HashMap<>();
        //小程序appId
        requestUrlParam.put("appid", ymlConfig.getAppId());
        //小程序secret
        requestUrlParam.put("secret", ymlConfig.getAppSecret());
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数：授权类型
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        String result = HttpUtil.get(CommonConstants.WX_SESSION_REQUEST_URL, requestUrlParam);
        return JsonUtil.toObject(result, WxSession.class);
    }

    @Override
    public void checkWxSession(WxSession wxSession) {
        if (wxSession.getErrcode() != null) {
            AssertUtil.isFalse(-1 == wxSession.getErrcode(), CommonErrorCode.WX_LOGIN_BUSY, wxSession.getErrmsg());
            AssertUtil.isFalse(40029 == wxSession.getErrcode(), CommonErrorCode.WX_LOGIN_INVALID_CODE, wxSession.getErrmsg());
            AssertUtil.isFalse(45011 == wxSession.getErrcode(), CommonErrorCode.WX_LOGIN_FREQUENCY_REFUSED, wxSession.getErrmsg());
            AssertUtil.isTrue(wxSession.getErrcode() == 0, CommonErrorCode.WX_LOGIN_UNKNOWN_ERROR, wxSession.getErrmsg());
        }
    }

    @Override
    public User getUser() {
        return new User(sessionUtils.getSessionData());
    }

    @Override
    @Transactional
    public void updateInfo(SessionData sessionData) {
        if(sessionData == null) throw new CommonException(CommonErrorCode.PARAM_ERROR);
        sessionData.setId(getUser().getId());
        log.info("修改用户信息，用户为{}", sessionData);
        sessionUtils.deleteSessionData();
        userMapper.updateById(new User(sessionData));
    }

    @Override
    public String upload(MultipartFile image) throws IOException {
        User user = getUser();
        if(user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        String url = aliossUtils.upload(image);
        if(url == null) throw new CommonException(CommonErrorCode.UPLOAD_FILE_FAIL);
        user.setAvatar(url);
        updateUser(user);
        return url;
    }

    @Override
    public void changeCatState(Integer state) {
        User user = getUser();
        if(user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        user.setCatState(state);
        updateUser(user);
    }

    @Override
    public Integer stroke() {
        //获取用户对象
        User user = getUser();
        if(user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        Integer newNum = user.getSmallDriedFish() + CommonConstants.STROKE_REWARD;
        user.setSmallDriedFish(newNum);
        updateUser(user);
        return newNum;
    }

    @Override
    public String signIn() {
        //获取用户对象
        User user = getUser();
        if(user == null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        //重复签到
        if(user.getSignIn() == 1) throw new CommonException(CommonErrorCode.DUPLICATE_SIGN_IN);
        user.setSignIn(1);
        user.setSmallDriedFish(user.getSmallDriedFish() + CommonConstants.SIGN_IN_REWARD);
        updateUser(user);
        String sentence = dailySentenceService.getSentence();
        if(sentence == null) throw new CommonException(CommonErrorCode.EMPTY_SENTENCE);
        return sentence;
    }

    @Override
    public void updateUser(User user) {
        sessionUtils.setSessionData(user);
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Override
    public void catHungryChange() {
        userMapper.catHungryChange();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetSignIn() {
        userMapper.resetSignIn();
    }
}
