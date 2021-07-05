package com.info.baymax.common.client;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * create by pengchuan.chen on 2021/6/21
 */
class JsonBuilder {
    private final ObjectMapper mapper;

    private static JsonBuilder _instance = new JsonBuilder();

    public static JsonBuilder getInstance() {
        return _instance;
    }

    public JsonBuilder() {
        mapper = buildObjectMapper(false);
    }

    public JsonBuilder pretty() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return this;
    }

    public <T> T fromJson(String json, Class<T> typeClass) {
        if (json == null) {
            throw new IllegalArgumentException("json string should not be null");
        }
        try {
            return mapper.readValue(json, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null) {
            throw new IllegalArgumentException("json string should not be null");
        }
        try {
            return mapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromObject(Object obj, Class<T> typeClass) {
        return fromJson(toJson(obj), typeClass);
    }

    public <T> T fromObject(Object obj, TypeReference<T> typeReference) {
        return fromJson(toJson(obj), typeReference);
    }

    public String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectMapper buildObjectMapper(boolean prettyJson) {
        ObjectMapper m = new ObjectMapper();
        if (prettyJson) {
            m.enable(SerializationFeature.INDENT_OUTPUT);
        }
        m.setSerializationInclusion(Include.NON_NULL);
        m.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        m.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        return m;
    }
}
