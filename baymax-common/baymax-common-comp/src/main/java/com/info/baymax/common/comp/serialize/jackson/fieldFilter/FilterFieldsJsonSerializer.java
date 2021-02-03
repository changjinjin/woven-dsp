package com.info.baymax.common.comp.serialize.jackson.fieldFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.core.annotation.JsonBody;

import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * jackson序列化处理器
 *
 * @author jingwei.yang
 * @date 2019年5月16日 下午5:28:53
 */
public class FilterFieldsJsonSerializer {
    private ObjectMapper objectMapper;

    private FilterFieldsJsonFilter jacksonFilter = new FilterFieldsJsonFilter();

    public FilterFieldsJsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 过滤逻辑
     *
     * @param clazz         过滤类型
     * @param includeFields 包含的字段
     * @param excludeFields 过滤掉的字段
     */
    public void filter(Class<?> clazz, String[] includeFields, String[] excludeFields) {
        if (clazz == null) {
            return;
        }
        if (includeFields != null && includeFields.length > 0) {
            jacksonFilter.include(clazz, includeFields);
        }
        if (excludeFields != null && excludeFields.length > 0) {
            jacksonFilter.exclude(clazz, excludeFields);
        }
        objectMapper.addMixIn(clazz, jacksonFilter.getClass());
    }

    /**
     * 执行过滤
     *
     * @param anno JsonBody注解实例
     */
    public void filter(JsonBody anno) {
        this.filter(anno.type(), anno.includes(), anno.excludes());
    }

    /**
     * 返回json
     *
     * @param object 返回对象
     * @return 序列化字符串
     * @throws JsonProcessingException
     */
    public String toJson(Object object) throws JsonProcessingException {
        objectMapper.setFilterProvider(jacksonFilter);
        return objectMapper.writeValueAsString(object);
    }

    /**
     * 返回json
     *
     * @param resultFile 输出文件
     * @param value      返回对象
     * @throws IOException
     */
    public void toJson(File resultFile, Object value) throws IOException {
        objectMapper.setFilterProvider(jacksonFilter);
        objectMapper.writeValue(resultFile, value);
    }

    /**
     * 返回json
     *
     * @param out   输出流
     * @param value 返回对象
     * @throws IOException
     */
    public void toJson(OutputStream out, Object value) throws IOException {
        objectMapper.setFilterProvider(jacksonFilter);
        objectMapper.writeValue(out, value);
    }

    /**
     * 返回json
     *
     * @param out   输出流
     * @param value 返回对象
     * @throws IOException
     */
    public void toJson(DataOutput out, Object value) throws IOException {
        objectMapper.setFilterProvider(jacksonFilter);
        objectMapper.writeValue(out, value);
    }
}