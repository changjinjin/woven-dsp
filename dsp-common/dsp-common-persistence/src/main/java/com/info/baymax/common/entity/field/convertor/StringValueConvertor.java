package com.info.baymax.common.entity.field.convertor;

import org.apache.commons.lang3.StringUtils;

import com.info.baymax.common.entity.field.MappedTypes;

@MappedTypes({String.class})
public class StringValueConvertor implements ValueConvertor<String> {

    @Override
    public String convert(String defaultValue) {
        return StringUtils.defaultString(defaultValue, "");
    }

}