package com.info.baymax.common.entity.field.convertor;

import com.info.baymax.common.entity.field.MappedTypes;

@MappedTypes({Null.class})
public class NullValueConvertor implements ValueConvertor<Object> {

    @Override
    public Object convert(String defaultValue) {
        throw new ValueConvertorException("No suitable converter was specified!");
    }

}
