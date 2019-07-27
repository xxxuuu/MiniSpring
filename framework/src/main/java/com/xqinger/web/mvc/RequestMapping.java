package com.xqinger.web.mvc;

import java.lang.annotation.*;

/**
 * URI映射
 * @author XUQING
 * @date 2019-07-24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    String value();
}
