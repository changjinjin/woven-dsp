package com.info.baymax.common.validation.passay;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class PassayMessageSource extends ResourceBundleMessageSource {

    public PassayMessageSource() {
        setBasenames("com.info.baymax.common.validation.passay.passay");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new PassayMessageSource(), Locale.getDefault());
    }
}
