package com.zlyx.easysocket.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * 
 * @Auth 赵光
 * @Describle
 * @2018年12月22日 下午4:37:56
 */
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface EasyBean {

	@AliasFor(annotation = Component.class, attribute = "value")
	String value() default "";
	
	/**
	 * 作用描述
	 * @return
	 */
	String[] todo();
}
