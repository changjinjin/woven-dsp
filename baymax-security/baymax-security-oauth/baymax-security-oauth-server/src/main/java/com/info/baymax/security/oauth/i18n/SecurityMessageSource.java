package com.info.baymax.security.oauth.i18n;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;

import java.util.Locale;

public class SecurityMessageSource extends SpringSecurityMessageSource {

	public SecurityMessageSource() {
        setBasenames("com.info.baymax.security.oauth.i18n.security");
	}

	public static MessageSourceAccessor getAccessor() {
		return new MessageSourceAccessor(new SecurityMessageSource(), Locale.SIMPLIFIED_CHINESE);
	}

}
