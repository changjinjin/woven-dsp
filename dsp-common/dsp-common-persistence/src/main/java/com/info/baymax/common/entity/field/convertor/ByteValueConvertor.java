package com.info.baymax.common.entity.field.convertor;

import com.info.baymax.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

@MappedTypes({Byte.class, byte.class})
public class ByteValueConvertor implements ValueConvertor<Byte> {

    @Override
    public Byte convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return null;
        }
        return Byte.valueOf(defaultValue);
    }

}
