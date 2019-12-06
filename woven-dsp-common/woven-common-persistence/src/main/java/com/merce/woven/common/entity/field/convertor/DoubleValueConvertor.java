package com.merce.woven.common.entity.field.convertor;

import com.merce.woven.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

@MappedTypes({Double.class, double.class})
public class DoubleValueConvertor implements ValueConvertor<Double> {

    @Override
    public Double convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return 0D;
        }
        return Double.valueOf(defaultValue);
    }

}
