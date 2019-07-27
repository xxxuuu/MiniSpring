package com.xqinger.web.handler;

import com.xqinger.web.mvc.Controller;
import com.xqinger.web.mvc.RequestMapping;
import com.xqinger.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解管理
 * @author XUQING
 * @date 2019-07-27
 */
public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * 解析注解
     * @param classList
     */
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for(Class<?> cls : classList) {
            if(cls.isAnnotationPresent(Controller.class)) {
                parseHandlerFromController(cls);
            }
        }
    }

    /**
     * 解析Controller注解
     * @param cls
     */
    public static void parseHandlerFromController(Class<?> cls) {
        Method[] methods = cls.getMethods();
        // 遍历方法
        for(Method m : methods) {
            if(!m.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            String uri = m.getDeclaredAnnotation(RequestMapping.class).value();

            List<String> paramNameList = new ArrayList<>();
            // 遍历参数
            for(Parameter p : m.getParameters()) {
                if(p.isAnnotationPresent(RequestParam.class)) {
                    paramNameList.add(p.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);

            MappingHandler mappingHandler = new MappingHandler(cls, m, params, uri);

            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}
