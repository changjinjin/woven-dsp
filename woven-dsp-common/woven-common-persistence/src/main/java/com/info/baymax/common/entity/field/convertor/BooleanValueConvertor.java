package com.info.baymax.common.entity.field.convertor;

import com.info.baymax.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

@MappedTypes({Boolean.class, boolean.class})
public class BooleanValueConvertor implements ValueConvertor<Boolean> {

    @Override
    public Boolean convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(defaultValue);
    }

}
