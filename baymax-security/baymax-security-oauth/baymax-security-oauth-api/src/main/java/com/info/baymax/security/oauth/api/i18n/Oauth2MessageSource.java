package com.info.baymax.security.oauth.api.i18n;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class Oauth2MessageSource extends ResourceBundleMessageSource {

    public Oauth2MessageSource() {
        setBasenames("com.info.baymax.security.oauth.api.i18n.messages");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new Oauth2MessageSource(), Locale.getDefault());
    }

}
