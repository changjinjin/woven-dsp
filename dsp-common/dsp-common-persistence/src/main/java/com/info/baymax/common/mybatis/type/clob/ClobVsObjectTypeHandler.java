package com.info.baymax.common.mybatis.type.clob;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Clob VS Object TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:46
 */
public class ClobVsObjectTypeHandler<T> extends AbstractClobTypeHandler<T> {
    @Override
    public String translate2Str(T t) {
        return toJson(t);
    }

    @Override
    public T translate2Bean(String result) {
        return fromJson(result, new TypeReference<T>() {
        });
    }

}
