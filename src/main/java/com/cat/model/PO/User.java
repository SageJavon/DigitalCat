package com.cat.model.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cat.model.DTO.SessionData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String sessionId;

    private String openId;

    private String sessionKey;

    private String unionId;

    //用户基本信息
    private String nickname;

    private String avatar;

    private Integer gender;

    //数据信息
    private Integer smallDriedFish;

    private Integer catHungry;

    private Integer signIn;

    private Integer catState;

    private Integer favor;

    public User(SessionData sessionData) {
        this.id = sessionData.getId();
        this.sessionId = sessionData.getSessionId();
        this.openId = sessionData.getOpenId();
        this.nickname = sessionData.getNickname();
        this.avatar = sessionData.getAvatar();
        this.gender = sessionData.getGender();
        this.smallDriedFish = sessionData.getSmallDriedFish();
        this.catHungry = sessionData.getCatHungry();
        this.signIn = sessionData.getSignIn();
        this.catState = sessionData.getCatState();
        this.favor = sessionData.getFavor();
    }
}
