package com.info.baymax.common.entity.field.convertor;

import com.info.baymax.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

@MappedTypes({Float.class, float.class})
public class FloatValueConvertor implements ValueConvertor<Float> {

    @Override
    public Float convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return 0F;
        }
        return Float.valueOf(defaultValue);
    }

}
