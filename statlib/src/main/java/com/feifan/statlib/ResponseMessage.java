package com.feifan.statlib;

/**
 * Created by xuchunlei on 15/12/8.
 */
class ResponseMessage {

    /** 响应状态－成功 */
    public static final int RESPONSE_STATUS_SUCCESS = 0;

    int status; // 0表示成功，其他表示失败
    String msg; // 消息
}
