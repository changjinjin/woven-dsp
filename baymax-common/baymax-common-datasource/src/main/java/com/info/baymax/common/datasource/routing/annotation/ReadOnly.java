package com.info.baymax.common.datasource.routing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description 通过该接口注释的service使用读模式，其他使用写模式
 * <p>
 * 接口注释只是一种办法，如果项目已经有代码了，通过注释可以不修改任何业务代码加持读写分离
 * 也可以通过切面根据方法开头来设置读写模式，例如getXXX()使用读模式，其他使用写模式
 *
 * @author jingwei.yang
 * @date 2021年7月1日 下午5:18:10
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnly {
}
