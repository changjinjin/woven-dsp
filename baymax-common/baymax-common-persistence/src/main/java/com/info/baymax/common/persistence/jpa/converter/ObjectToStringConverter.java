package com.info.baymax.common.persistence.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 通用属性转换器，仅用于JPA生成表结构时表明数据类型，没有实现具体的转换逻辑
 *
 * @author jingwei.yang
 * @date 2019年9月30日 上午10:35:21
 */
@Converter
public class ObjectToStringConverter implements AttributeConverter<Object, String> {

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        throw new JpaAttributeConvertException("Not Supported! Used only to create table structures!");
    }

    @Override
    public Object convertToEntityAttribute(String data) {
        throw new JpaAttributeConvertException("Not Supported! Used only to create table structures!");
    }
}
