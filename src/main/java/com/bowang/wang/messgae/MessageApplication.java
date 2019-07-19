package com.bowang.wang.messgae;

import com.bowang.wang.messgae.model.PushMessage;
import com.bowang.wang.messgae.service.SocketIOServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

/**
 * @author zheng
 */
@SpringBootApplication
public class MessageApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(MessageApplication.class, args);
        communicateWithBrowser();
    }

    /**
     * 和浏览器交流线程
     */
    private static void communicateWithBrowser(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                PushMessage pushMessage79 = new PushMessage();
                PushMessage pushMessage80 = new PushMessage();
                pushMessage79.setLoginUserNum("79");
                pushMessage80.setLoginUserNum("80");
                Scanner sc = new Scanner(System.in);
                //利用hasNextXXX()判断是否还有下一输入项
                while (sc.hasNext()) {
                    //利用nextXXX()方法输出内容
                    String str = sc.next();
                    pushMessage79.setContent(str);
                    SocketIOServiceImpl.pushMessageToUser(pushMessage79);
                    pushMessage80.setContent(str);
                    SocketIOServiceImpl.pushMessageToUser(pushMessage80);
                }
            }
        });
        thread.start();
    }

}
