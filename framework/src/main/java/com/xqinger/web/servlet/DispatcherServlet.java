package com.xqinger.web.servlet;

import javax.servlet.*;

import com.xqinger.web.handler.HandlerManager;
import com.xqinger.web.handler.MappingHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author XUQING
 * @date 2019-07-23
 */
public class DispatcherServlet implements Servlet {
    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        // 遍历所有Controller
        for(MappingHandler handler : HandlerManager.mappingHandlerList) {
            try {
                if(handler.handler(req, res)) {
                    return;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
