package com.xqinger.web.server;

import com.xqinger.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * 启动与配置内嵌的Tomcat
 * @author XUQING
 * @date 2019-07-23
 */
public class TomcatServer {
    private Tomcat tomcat;
    private String[] args;

    public TomcatServer(String[] args) {
        this.args = args;
    }

    public void startServer() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.start();

        // 配置Tomcat
        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        DispatcherServlet servlet = new DispatcherServlet();
        Tomcat.addServlet(context, "dispatcherServlet", servlet).setAsyncSupported(true);
        context.addServletMappingDecoded("/", "dispatcherServlet");
        tomcat.getHost().addChild(context);

        // 保持Tomcat不被关闭
        Thread awaitThread = new Thread("tomcat_await_thread") {
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
