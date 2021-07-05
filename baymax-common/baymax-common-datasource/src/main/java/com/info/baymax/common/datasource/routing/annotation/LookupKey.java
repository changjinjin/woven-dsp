package com.info.baymax.common.datasource.routing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解指定类或方法优先使用的数据源 LookupKey，如果没有找到指定的名称则使用默认的
 *
 * @author jingwei.yang
 * @date 2021年7月2日 上午12:27:02
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LookupKey {

	/**
	 * 指定lookupKey：如 master,slave0,slave1等
	 *
	 * @return 指定的数据源
	 */
	String value() default "master";
}
