package com.info.baymax.common.mybatis.type.base64.clob;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.mybatis.type.TypeHandleException;
import com.info.baymax.common.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.mybatis.type.clob.AbstractClobTypeHandler;

public abstract class GZBase64ClobVsObjectTypeHandler<T> extends AbstractClobTypeHandler<T> implements GZBase64Parser {

    protected Class<T> valueClass;

    @SuppressWarnings("unchecked")
    public GZBase64ClobVsObjectTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        valueClass = (Class<T>) params[0];
    }

    @Override
    public String translate2Str(T t) {
        try {
            return encode(JSON.toJSONString(t));
        } catch (IOException e) {
            throw new TypeHandleException(e);
        }
    }

    @Override
    public T translate2Bean(String result) {
        try {
            String decode = decode(result);
            if (StringUtils.isEmpty(decode)) {
                return null;
            }
            return JSON.parseObject(decode, valueClass);
        } catch (IOException e) {
            throw new TypeHandleException(e);
        }
    }
}
