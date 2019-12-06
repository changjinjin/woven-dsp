package com.merce.woven.common.entity.field.convertor;

public class UnknownValueConvertor implements ValueConvertor<Object> {

    @Override
    public Object convert(String defaultValue) {
        throw new ValueConvertorException("No suitable converter was specified!");
    }

}
