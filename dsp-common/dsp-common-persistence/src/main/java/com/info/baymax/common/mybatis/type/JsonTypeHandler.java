package com.info.baymax.common.mybatis.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.utils.JsonBuilder;

public interface JsonTypeHandler {

    default <O> String toJson(O t) {
        try {
            return JsonBuilder.getInstance().toJson(t);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <O> O fromJson(String json, TypeReference<O> typeReference) {
        try {
            return JsonBuilder.getInstance().fromJson(json, typeReference);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

}
