package com.xqinger.beans;

import java.lang.annotation.*;

/**
 * 自动装配
 * @author XUQING
 * @date 2019-07-28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {
}
