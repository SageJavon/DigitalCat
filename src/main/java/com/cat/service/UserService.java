package com.cat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cat.model.DTO.SessionData;
import com.cat.model.DTO.WxSession;
import com.cat.model.PO.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService extends IService<User> {

    SessionData login(String code);

    WxSession getWxSessionByCode(String code);

    void checkWxSession(WxSession wxSession);

    User getUser();

    void updateInfo(SessionData user);

    String upload(MultipartFile image) throws IOException;

    void changeCatState(Integer state);

    Integer stroke();

    String signIn();

    void resetSignIn();

    void catHungryChange();

    void updateUser(User user);
}
