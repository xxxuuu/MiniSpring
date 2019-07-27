package com.xqinger.starter;

import com.xqinger.beans.BeanFactory;
import com.xqinger.core.ClassScanner;
import com.xqinger.web.handler.HandlerManager;
import com.xqinger.web.server.TomcatServer;

import java.util.List;

/**
 * @author XUQING
 * @date 2019-07-22
 */
public class MiniApplication {
    public static void run(Class<?> cls, String[] args) {
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            BeanFactory.initBean(classList);
            HandlerManager.resolveMappingHandler(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
