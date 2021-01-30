package com.qa.basic.annos;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DubboAPI {
    String api();

    String service() default "";

    String method() default "";

    String group() default "";

    String[] paramTypes();

    String schema() default "";
}

