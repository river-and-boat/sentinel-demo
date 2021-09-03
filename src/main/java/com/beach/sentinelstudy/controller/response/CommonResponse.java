package com.beach.sentinelstudy.controller.response;

public class CommonResponse {
    public Object data;
    public int code;

    public CommonResponse(Object data, int code) {
        this.data = data;
        this.code = code;
    }
}
