package com.xqinger.web.handler;

import com.xqinger.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Mapping注解
 * @author XUQING
 * @date 2019-07-26
 */
public class MappingHandler {
    /** 类 */
    private Class<?> controllerClass;
    /** 方法 */
    private Method method;
    /** 参数（Mapping的参数 非方法参数） */
    private String[] args;
    /** URI */
    private String uri;

    public MappingHandler(Class<?> controllerClass, Method method, String[] args, String uri) {
        this.controllerClass = controllerClass;
        this.method = method;
        this.args = args;
        this.uri = uri;
    }

    /**
     * 注解处理
     * @param req
     * @param res
     * @return
     */
    public boolean handler(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        HttpServletRequest httpReq = (HttpServletRequest) req;

        // URI
        if(!uri.equals(httpReq.getRequestURI())) {
            return false;
        }

        // 参数
        Object[] param = new Object[args.length];
        for(int i = 0; i < args.length; i++) {
            param[i] = req.getParameter(args[i]);
        }

        Object controller = BeanFactory.getBean(controllerClass);
        Object response = method.invoke(controller, param);

        res.getWriter().println(response.toString());
        return true;
    }
}
