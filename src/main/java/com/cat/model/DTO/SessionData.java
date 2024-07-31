package com.cat.model.DTO;

import com.cat.common.CommonErrorCode;
import com.cat.utils.AssertUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.cat.model.PO.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionData {

    /**
     * {@link User}
     */
    private Long id;

    private String sessionId;

    private String openId;

    private String nickname;

    private String avatar;

    private Integer gender;

    private Integer smallDriedFish;

    private Integer catHungry;

    private Integer signIn;

    private Integer catState;

    private Integer favor;

    //通过user对象构建sessionData对象
    public SessionData(User user){
        AssertUtil.isNotNull(user, CommonErrorCode.USER_NOT_EXIST);
        id = user.getId();
        sessionId = user.getSessionId();
        openId = user.getOpenId();
        nickname = user.getNickname();
        avatar = user.getAvatar();
        gender = user.getGender();
        smallDriedFish = user.getSmallDriedFish();
        catHungry = user.getCatHungry();
        signIn = user.getSignIn();
        catState = user.getCatState();
        favor = user.getFavor();
    }
}
