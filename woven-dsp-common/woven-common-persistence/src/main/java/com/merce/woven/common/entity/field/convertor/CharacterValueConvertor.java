package com.merce.woven.common.entity.field.convertor;

import com.merce.woven.common.entity.field.MappedTypes;
import org.apache.commons.lang3.StringUtils;

@MappedTypes({Character.class, char.class})
public class CharacterValueConvertor implements ValueConvertor<Character> {

    @Override
    public Character convert(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return null;
        }
        return Character.valueOf(defaultValue.charAt(0));
    }

}
