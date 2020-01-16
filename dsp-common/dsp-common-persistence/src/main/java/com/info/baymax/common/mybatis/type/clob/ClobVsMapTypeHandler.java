package com.info.baymax.common.mybatis.type.clob;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public abstract class ClobVsMapTypeHandler<K, V> extends AbstractClobTypeHandler<Map<K, V>> {

    protected Class<K> keyClass;
    protected Class<V> valueClass;

    @SuppressWarnings("unchecked")
    public ClobVsMapTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        keyClass = (Class<K>) params[0];
        valueClass = (Class<V>) params[1];
    }

    @Override
    public String translate2Str(Map<K, V> t) {
        return JSON.toJSONString(t);
    }

    @Override
    public Map<K, V> translate2Bean(String result) {
        Map<K, V> map = new HashMap<K, V>();
        JSONObject obj = JSON.parseObject(result);
        obj.forEach((k, v) -> {
            if (v != null) {
                map.put(toKey(k), toValue(v));
            } else {
                map.put(toKey(k), null);
            }
        });
        return map;
    }

    public abstract K toKey(String k);

    public abstract V toValue(Object v);

}
