package com.zlyx.easysocket.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 *  @auth 赵光
 *  @Desc 注释注解(可用于描述类、方法、字段)
 *  @Date 2018年11月15日 下午2:10:59
 */
@Documented
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.FIELD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Desc("注释注解(可用于描述类、方法、字段)")
public @interface Desc {
	
	String[] value();

}
