package com.cat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxSession {

    private String openId;

    private String sessionKey;

    private String unionId;

    private Integer errcode;

    private String errmsg;
}
