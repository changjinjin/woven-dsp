package com.info.baymax.common.validation.passay;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.passay.PropertiesMessageResolver;
import org.springframework.context.support.MessageSourceAccessor;

import javax.annotation.Nullable;

public class PassayPropertiesMessageResolver extends PropertiesMessageResolver {

    @Setter
    @Nullable
    private MessageSourceAccessor accessor;

    public PassayPropertiesMessageResolver(MessageSourceAccessor accessor) {
        super();
        this.accessor = accessor;
    }

    @Override
    public String getMessage(String key) {
        String message = null;
        if (accessor != null) {
            message = accessor.getMessage(key);
            if (StringUtils.isNotEmpty(message)) {
                return message;
            }
        }
        return super.getMessage(key);
    }
}
