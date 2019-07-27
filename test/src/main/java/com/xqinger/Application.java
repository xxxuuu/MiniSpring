package com.xqinger;


import com.xqinger.starter.MiniApplication;
import com.xqinger.web.server.TomcatServer;

/**
 * @author XUQING
 * @date 2019-07-22
 */
public class Application {
    public static void main(String[] args) {
        MiniApplication.run(Application.class, args);
    }
}
