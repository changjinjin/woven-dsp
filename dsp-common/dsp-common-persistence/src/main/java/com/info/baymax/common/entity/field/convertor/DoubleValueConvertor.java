package com.info.baymax.common.entity.field.convertor;

import com.info.baymax.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

@MappedTypes({Double.class, double.class})
public class DoubleValueConvertor implements ValueConvertor<Double> {

    @Override
    public Double convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return null;
        }
        return Double.valueOf(defaultValue);
    }

}
