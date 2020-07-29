package com.info.baymax.common.mybatis.type;
import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.utils.JsonUtils;

import java.util.Collection;
import java.util.Map;

public interface JsonFormatTypeHandler {

    default <O> String toJson(O t) {
        try {
            return JsonUtils.toJson(t);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <M extends Map<K, V>, K, V> M fromJson(String json, Class<M> mCkass, Class<K> kClass, Class<V> vClass) {
        try {
            return JsonUtils.fromJson(json, JsonUtils.contructMapType(mCkass, kClass, vClass));
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <C extends Collection<O>, O> C fromJson(String json, Class<C> cClass, Class<O> oClass) {
        try {
            return JsonUtils.fromJson(json, JsonUtils.contructCollectionType(cClass, oClass));
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <O> O fromJson(String json, Class<O> tClass) {
        try {
            return JsonUtils.fromJson(json, tClass);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <O> O fromJson(String json, TypeReference<O> typeReference) {
        try {
            return JsonUtils.fromJson(json, typeReference);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }
}
