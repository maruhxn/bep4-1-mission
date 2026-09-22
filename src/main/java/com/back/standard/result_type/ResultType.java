package com.back.standard.result_type;

public interface ResultType {
    String getResultCode();

    String getMsg();

    default <T> T getData() {
        return null;
    }
}
