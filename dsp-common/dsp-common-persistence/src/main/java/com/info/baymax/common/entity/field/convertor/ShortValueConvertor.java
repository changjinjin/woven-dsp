package com.info.baymax.common.entity.field.convertor;

import com.info.baymax.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

@MappedTypes({Short.class, short.class})
public class ShortValueConvertor implements ValueConvertor<Short> {

    @Override
    public Short convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return (short) 0;
        }
        return Short.valueOf(defaultValue);
    }

}
