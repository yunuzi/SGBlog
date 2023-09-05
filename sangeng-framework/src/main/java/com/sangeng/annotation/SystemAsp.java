package com.sangeng.annotation;

import net.bytebuddy.dynamic.DynamicType;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public  @interface SystemAsp {

    String businessName();
}
