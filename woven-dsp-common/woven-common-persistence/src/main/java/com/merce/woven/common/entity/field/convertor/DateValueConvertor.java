package com.merce.woven.common.entity.field.convertor;

import com.merce.woven.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@MappedTypes({Date.class})
public class DateValueConvertor implements ValueConvertor<Date> {

    @Override
    public Date convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return null;
        }
        try {
            return Date.from(LocalDateTime.parse(defaultValue).atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new ValueConvertorException(e.getMessage(), e);
        }
    }

}
