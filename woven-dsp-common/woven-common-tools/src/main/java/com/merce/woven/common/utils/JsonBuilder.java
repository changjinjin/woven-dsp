package com.merce.woven.common.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.merce.woven.common.dataflow.event.FlowEvent;
import com.merce.woven.common.dataflow.event.FlowEventMixIn;

public class JsonBuilder {

    private ObjectMapper mapper = new ObjectMapper();

    private static JsonBuilder _instance = new JsonBuilder();

    public static JsonBuilder getInstance() {
        return _instance;
    }

    public JsonBuilder() {
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.addMixInAnnotations(FlowEvent.class, FlowEventMixIn.class);
    }

    public JsonBuilder pretty() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return this;
    }

    public <T> T fromJson(String json, Class<T> typeOfT) {
        if (json == null) {
            throw new IllegalArgumentException("json string should not be null");
        }
        try {
            return mapper.readValue(json, typeOfT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String json, TypeReference valueTypeRef) {
        if (json == null) {
            throw new IllegalArgumentException("json string should not be null");
        }
        try {
            return mapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        m.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        m.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        m.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        m.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        m.setSerializationInclusion(Include.NON_NULL);
        return m;
    }

    public static ObjectMapper defaultObjectMapper = buildObjectMapper(false);

    /**
     * parse object by fastjson api
     * @param typeOfT
     * @param <T>
     * @return
     */
    public <T> T parseFastJsonObject(String json, Class<T> typeOfT){
        if (json == null) {
            throw new IllegalArgumentException("json string should not be null");
        }
        try {
            return JSONObject.parseObject(json, typeOfT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * to json string by fastjson api
     * @param obj
     * @return
     */
    public String toFastJsonString(Object obj){
        try {
            return JSONObject.toJSONString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
