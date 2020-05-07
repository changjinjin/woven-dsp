package com.info.baymax.common.entity.field.convertor;

import com.info.baymax.common.entity.field.MappedTypes;

@MappedTypes({String.class})
public class StringValueConvertor implements ValueConvertor<String> {

    @Override
    public String convert(String defaultValue) {
        return defaultValue;
    }

}
