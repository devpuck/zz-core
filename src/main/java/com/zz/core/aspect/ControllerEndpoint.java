package com.zz.core.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerEndpoint {

    String operation() default "";
    boolean recordResponse() default false;
    String exceptionMessage() default "系统内部异常";
}
