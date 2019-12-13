package com.info.baymax.dsp.auth.security.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * @author jingwei.yang
 * @date 2019年6月18日 上午10:34:22
 */
public class AuthMethodNotSupportedException extends AuthenticationServiceException {
    private static final long serialVersionUID = 3705043083010304496L;

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
