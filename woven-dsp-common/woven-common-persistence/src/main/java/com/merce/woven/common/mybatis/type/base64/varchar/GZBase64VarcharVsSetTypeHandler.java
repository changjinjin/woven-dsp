package com.merce.woven.common.mybatis.type.base64.varchar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.merce.woven.common.mybatis.type.TypeHandleException;
import com.merce.woven.common.mybatis.type.base64.GZBase64Parser;
import com.merce.woven.common.mybatis.type.varchar.AbstractVarcharTypeHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public abstract class GZBase64VarcharVsSetTypeHandler<T> extends AbstractVarcharTypeHandler<Set<T>>
    implements GZBase64Parser {

    protected Class<T> valueClass;

    @SuppressWarnings("unchecked")
    public GZBase64VarcharVsSetTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        valueClass = (Class<T>) params[0];
    }

    @Override
    public String translate2Str(Set<T> t) {
        try {
            return encode(JSON.toJSONString(t));
        } catch (IOException e) {
            e.printStackTrace();
            throw new TypeHandleException(e);
        }
    }

    @Override
    public Set<T> translate2Bean(String result) {
        try {
            String decode = decode(result);
            if (StringUtils.isEmpty(decode)) {
                return null;
            }
            return new HashSet<T>(JSONArray.parseArray(decode, valueClass));
        } catch (IOException e) {
            e.printStackTrace();
            throw new TypeHandleException(e);
        }
    }
}
