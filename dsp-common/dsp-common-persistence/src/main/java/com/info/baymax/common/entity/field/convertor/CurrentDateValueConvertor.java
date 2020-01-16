package com.info.baymax.common.entity.field.convertor;

import java.util.Date;

public class CurrentDateValueConvertor implements ValueConvertor<Date> {

    @Override
    public Date convert(String defaultValue) {
        return new Date();
    }
}
