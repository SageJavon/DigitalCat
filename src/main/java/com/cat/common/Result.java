package com.cat.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.cat.common.serializer.EnumSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

    @JSONField(serializeUsing = EnumSerializer.class)
    private CommonErrorCode commonErrorCode;

    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }

    public static <T> Result<T> success(String mess, T data) {
        Result<T> m = new Result<>();
        m.setCode(0);
        m.setData(data);
        m.setMsg(mess);
        return m;
    }

    public static <T> Result<T> fail(String mess) {
        return fail(mess, null);
    }

    public static <T> Result<T> fail(String mess, T data) {
        Result<T> m = new Result<>();
        m.setCode(-1);
        m.setData(data);
        m.setMsg(mess);

        return m;
    }

    public static <T> Result<T> fail(CommonErrorCode commonErrorCode, String mess) {
        Result<T> m = new Result<>();
        m.setCode(-1);
        m.setCommonErrorCode(commonErrorCode);
        m.setData(null);
        m.setMsg(mess);

        return m;
    }

    public static <T> Result<T> result(CommonErrorCode commonErrorCode, T data) {
        Result<T> m = new Result<>();
        m.setCode(-1);
        m.setData(data);
        m.setCommonErrorCode(commonErrorCode);

        return m;
    }

    public static <T> Result<T> result(CommonErrorCode commonErrorCode) {
        return result(commonErrorCode,null);
    }
}