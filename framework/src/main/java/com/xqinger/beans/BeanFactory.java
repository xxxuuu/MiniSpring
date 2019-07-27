package com.xqinger.beans;

import com.xqinger.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean工厂
 * @author XUQING
 * @date 2019-07-28
 */
public class BeanFactory {
    private static Map<Class<?>, Object> class2Bean = new ConcurrentHashMap<>();

    public static Object getBean(Class<?> cls) {
        return class2Bean.get(cls);
    }

    /**
     * 初始化所有Bean
     * @param classList 启动时扫描到的Class列表
     * @throws Exception
     */
    public static void initBean(List<Class<?>> classList) throws Exception {
        // TODO [功能] 解决循环依赖
        // TODO [重构] 使用数据结构优化
        List<Class<?>> createList = new ArrayList<>(classList);
        while (createList.size() > 0) {
            int remainSize = createList.size();
            // 完成创建 则删除
            Iterator<Class<?>> it = createList.iterator();
            while (it.hasNext()) {
                if(finishCreate(it.next())) {
                    it.remove();
                }
            }

            if(createList.size() == remainSize) {
                throw new Exception("循环依赖");
            }
        }
    }

    /**
     * 创建Bean
     * @param cls
     * @return 是否创建成功
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        // 不是Bean或Controller 不需要初始化
        if(!(cls.isAnnotationPresent(Bean.class) || cls.isAnnotationPresent(Controller.class))) {
            return true;
        }

        Object bean = cls.newInstance();
        // 遍历字段
        for(Field field : cls.getDeclaredFields()) {
            if(field.isAnnotationPresent(AutoWired.class)) {
                Class<?> fieldType = field.getType();
                // 获取依赖
                Object reliantBean = BeanFactory.getBean(fieldType);
                // 不存在 创建失败
                if(reliantBean == null) {
                    return false;
                }
                field.setAccessible(true);
                field.set(bean, reliantBean);
            }
        }
        class2Bean.put(cls, bean);

        return true;
    }
}
