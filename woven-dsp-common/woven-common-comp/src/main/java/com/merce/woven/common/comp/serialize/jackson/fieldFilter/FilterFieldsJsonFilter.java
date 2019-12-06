package com.merce.woven.common.comp.serialize.jackson.fieldFilter;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.merce.woven.common.comp.utils.ProxyTargetUtils;

import java.util.*;

/**
 * jackson 中， 我们可以通过 ObjectMapper.setFilterProvider 来进行过滤规则的设置， jackson 内置了一个
 * SimpleFilterProvider 过滤器，这个过滤器功能比较单一， 不能很好的支持我们想要的效果。这里自己实现了一个过滤器
 * JacksonJsonFilter.
 *
 * @author jingwei.yang
 * @date 2019年5月16日 下午5:26:18
 */
@SuppressWarnings("deprecation")
@JsonFilter("FilterFieldsJsonFilter")
public class FilterFieldsJsonFilter extends FilterProvider {

    Map<Class<?>, Set<String>> includeMap = new HashMap<>();
    Map<Class<?>, Set<String>> excludeMap = new HashMap<>();

    // 包含某些字段
    public void include(Class<?> type, String[] fields) {
        addToMap(includeMap, type, fields);
    }

    // 过滤某些字段
    public void exclude(Class<?> type, String[] fields) {
        addToMap(excludeMap, type, fields);
    }

    private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type, String[] fields) {
        Set<String> fieldSet = map.getOrDefault(type, new HashSet<>());
        fieldSet.addAll(Arrays.asList(fields));
        map.put(type, fieldSet);
    }

    @Override
    public BeanPropertyFilter findFilter(Object filterId) {
        throw new UnsupportedOperationException("Access to deprecated filters not supported");
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {
        return new SimpleBeanPropertyFilter() {
            @Override
            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov, PropertyWriter writer) throws Exception {
                /**
                 * 这里的对象可能是AOP处理的之后的对象，也可能是mybatis返回的代理对象，这里需要拿到该对象的真实类型，否则放进过滤器集合中的类型无法匹配到该对象以至于字段过滤失效
                 */
                Class<?> targetClass = ProxyTargetUtils.getTargetClass(pojo);
                if (apply(targetClass, writer.getName())) {
                    writer.serializeAsField(pojo, jgen, prov);
                } else if (!jgen.canOmitFields()) {
                    writer.serializeAsOmittedField(pojo, jgen, prov);
                }
            }
        };
    }

    public boolean apply(Class<?> type, String name) {
        Set<String> includeFields = includeMap.get(type);
        Set<String> excludeFields = excludeMap.get(type);
        return (includeFields != null && includeFields.contains(name)) || (excludeFields != null && !excludeFields.contains(name)) || (includeFields == null && excludeFields == null);
    }

}
