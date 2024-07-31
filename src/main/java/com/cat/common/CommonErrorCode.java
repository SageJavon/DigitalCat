package com.cat.common;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CommonErrorCode {

    //微信相关错误
    WX_LOGIN_BUSY(1002,"系统繁忙，此时请开发者稍候再试","微信小程序繁忙，请稍候再试"),
    WX_LOGIN_INVALID_CODE(1003,"无效的code","授权失败，请检查微信账号是否正常"),
    WX_LOGIN_FREQUENCY_REFUSED(1004,"请求太频繁，一分钟不能超过100次","请勿多次重复授权"),
    WX_LOGIN_UNKNOWN_ERROR(1005,"微信授权未知错误","微信异常，请稍后再试"),
    WX_APP_SECRET_INVALID(1006,"AppSecret 错误或者 AppSecret 不属于这个小程序","系统异常，请稍后再试"),
    WX_GRANT_TYPE_MUSTBE_CLIENTCREDENTIAL(1007,"请确保 grant_type 字段值为 client_credential","系统异常，请稍后再试"),
    WX_APPID_INVALID(1008,"不合法的 AppID","系统异常，请稍后再试"),
    WX_CONTENT_ILLEGAL(1009,"内容安全校验不通过","内容含有违法违规内容，请修改"),
    WX_CONTENT_SECURITY_FORMAT_ERROR(1010,"内容安全校验格式不合法","系统异常，请稍后再试"),

    //会话相关错误
    INVALID_SESSION(2000,"会话丢失","登录已失效，请重新登录"),

    //文件相关错误
    UPLOAD_FILE_FAIL(2001,"上传文件失败","请检查网络状况后稍后重试"),

    //用户相关错误
    USER_NOT_EXIST(2002,"用户不存在","用户不存在"),
    USER_ID_EMPTY(2003, "用户ID为空", "出错啦，请联系管理员"),
    FISH_NOT_ENOUGH(2004, "小鱼干数量不足", "小鱼干数量不足"),
    DUPLICATE_SIGN_IN(2005, "重复签到", "今日已签到"),
    EMPTY_SENTENCE(2006, "句子为空", "出错啦，请联系开发者"),

    //物品相关错误
    ITEM_TYPE_ERROR(3001, "物品类型错误", "物品类型错误"),
    ITEM_ID_EMPTY(3002, "物品ID为空", "物品ID为空"),
    ITEM_NOT_EXIST(3003, "物品不存在", "物品不存在"),
    FOOD_NOT_ENOUGH(3004, "食物数量不足", "食物数量不足"),
    ITEM_ALREADY_OBTAINED(3005, "物品已拥有", "物品已拥有"),
    TOY_NOT_ENOUGH(3010, "玩具数量不足", "玩具数量不足"),

    //模型接口错误
    API_KEY_NOT_FOUND(3006, "找不到api key", "后台错误"),
    TIME_LIMIT_EXCEEDED(3007, "连接超时", "连接超时"),
    ENCODE_ERROR(3008, "编码错误", "编码错误"),

    //悄悄话相关错误
    CONTENT_EMPTY(3009, "悄悄话内容为空", "悄悄话内容为空"),

    //参数错误
    PARAM_ERROR(5000, "参数错误", "参数错误")

    ;
    /**
     * 错误码
     */
    private final Integer errorCode;

    /**
     * 错误原因（给开发看的）
     */
    private final String errorReason;

    /**
     * 错误行动指示（给用户看的）
     */
    private final String errorSuggestion;

    CommonErrorCode(Integer errorCode, String errorReason, String errorSuggestion) {
        this.errorCode = errorCode;
        this.errorReason = errorReason;
        this.errorSuggestion = errorSuggestion;
    }

    @Override
    public String toString() {
        return "CommonErrorCode{" +
                "errorCode=" + errorCode +
                ", errorReason='" + errorReason + '\'' +
                ", errorSuggestion='" + errorSuggestion + '\'' +
                '}';
    }

    //use for json serialization
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("errorCode",errorCode);
        map.put("errorReason",errorReason);
        map.put("errorSuggestion",errorSuggestion);
        return map;
    }


}