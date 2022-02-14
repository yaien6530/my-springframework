package com.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1> 是否单例 </h1>
 * 默认单例，或者填singleton则为单例
 *
 * @author aieny
 * @date 2022/2/12
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    String value() default "";
}
