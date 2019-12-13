package com.info.baymax.common.comp.serialize.jackson.javassist;

import java.lang.reflect.Method;

public interface FilterPropertyHandler {

    /**
     * 过滤属性方法
     *
     * @param method 执行的方法
     * @param object 过滤的对象
     * @return 过滤结果
     */
    public Object filterProperties(Method method, Object object);
}
