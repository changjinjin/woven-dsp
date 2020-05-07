package com.info.baymax.common.entity.field.convertor;

import com.info.baymax.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

@MappedTypes({Long.class, long.class})
public class LongValueConvertor implements ValueConvertor<Long> {

    @Override
    public Long convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return null;
        }
        return Long.valueOf(defaultValue);
    }

}
