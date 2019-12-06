package com.merce.woven.common.entity.field.convertor;

import com.merce.woven.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

@MappedTypes({Integer.class, int.class})
public class IntegerValueConvertor implements ValueConvertor<Integer> {

    @Override
    public Integer convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return 0;
        }
        return Integer.valueOf(defaultValue);
    }

}
