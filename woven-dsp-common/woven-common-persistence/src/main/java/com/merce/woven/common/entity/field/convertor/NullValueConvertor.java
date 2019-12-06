package com.merce.woven.common.entity.field.convertor;

import com.merce.woven.common.entity.field.MappedTypes;

@MappedTypes({Null.class})
public class NullValueConvertor implements ValueConvertor<Object> {

    @Override
    public Object convert(String defaultValue) {
        throw new ValueConvertorException("No suitable converter was specified!");
    }

}
