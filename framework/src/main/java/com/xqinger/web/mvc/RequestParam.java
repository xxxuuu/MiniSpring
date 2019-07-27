package com.xqinger.web.mvc;

import java.lang.annotation.*;

/**
 * 请求的参数
 * @author XUQING
 * @date 2019-07-24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    String value();
}
