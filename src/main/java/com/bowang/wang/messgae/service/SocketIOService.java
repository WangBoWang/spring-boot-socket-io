package com.bowang.wang.messgae.service;

import com.bowang.wang.messgae.model.PushMessage;

/**
 * @author wangb
 * @version 1.0.0
 * @since 2019/7/19
 */
public interface SocketIOService {

    //推送的事件
    public static final String PUSH_EVENT = "push_event";

    // 启动服务
    void start() throws Exception;

    // 停止服务
    void stop();

    // 推送信息
    void pushMessageToUser(PushMessage pushMessage);
}
