package com.back.global.dto;

import com.back.standard.result_type.ResultType;

public record RsData<T>(
        String resultCode,
        String msg,
        T data
) implements ResultType {
    public RsData(String resultCode, String msg) {
        this(resultCode, msg, null);
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
