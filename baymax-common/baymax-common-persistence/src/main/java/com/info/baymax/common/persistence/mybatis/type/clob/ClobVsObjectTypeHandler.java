package com.info.baymax.common.persistence.mybatis.type.clob;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Clob VS Object TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:46
 */
public class ClobVsObjectTypeHandler<T> extends AbstractClobTypeHandler<T> {
    protected Class<T> valueClass;

    @SuppressWarnings("unchecked")
    public ClobVsObjectTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        valueClass = (Class<T>) params[0];
    }

    @Override
    public String translate2Str(T t) {
        return toJson(t);
    }

    @Override
    public T translate2Bean(String result) {
        return fromJson(result, valueClass);
    }

}
