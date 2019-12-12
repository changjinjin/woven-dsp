package com.merce.woven.dsp.auth.security.i18n;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;

import java.util.Locale;

public class SecurityMessageSource extends SpringSecurityMessageSource {

	public SecurityMessageSource() {
		setBasenames("i18n.messages");
	}

	public static MessageSourceAccessor getAccessor() {
		return new MessageSourceAccessor(new SecurityMessageSource(), Locale.SIMPLIFIED_CHINESE);
	}

}
