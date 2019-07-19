package com.bowang.wang.messgae.service;

import com.bowang.wang.messgae.model.PushMessage;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangb
 * @version 1.0.0
 * @since 2019/7/19
 */
@Component
public class SocketIOServiceImpl{

    // 用来存已连接的客户端
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     *
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() throws Exception {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     *
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    public void start(){
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String loginUserNum = getParamsByClient(client);
            if (loginUserNum != null) {
                System.out.println(loginUserNum);
                System.out.println("SessionId:  " + client.getSessionId());
                System.out.println("RemoteAddress:  " + client.getRemoteAddress());
                System.out.println("Transport:  " + client.getTransport());
                clientMap.put(loginUserNum, client);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String loginUserNum = getParamsByClient(client);
            if (loginUserNum != null) {
                clientMap.remove(loginUserNum);
                System.out.println("断开连接： " + loginUserNum);
                System.out.println("断开连接： " + client.getSessionId());
                client.disconnect();
            }
        });

        // 处理自定义的事件，与连接监听类似
        socketIOServer.addEventListener("text", Object.class, (client, data, ackSender) -> {
            // TODO do something
            client.getHandshakeData();
            System.out.println( " 客户端：************ " + data);
        });
        socketIOServer.start();
    }


    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    public static void pushMessageToUser(PushMessage pushMessage) {
        String loginUserNum = pushMessage.getLoginUserNum();
        if (StringUtils.isNotBlank(loginUserNum)) {
            SocketIOClient client = clientMap.get(loginUserNum);
            if (client != null){
                client.sendEvent("push_event", pushMessage);
            }
        }
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     * @param client
     * @return
     */
    private  String getParamsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的loginUserNum必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("loginUserNum");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public static Map<String, SocketIOClient> getClientMap() {
        return clientMap;
    }

    public static void setClientMap(Map<String, SocketIOClient> clientMap) {
        SocketIOServiceImpl.clientMap = clientMap;
    }

}
