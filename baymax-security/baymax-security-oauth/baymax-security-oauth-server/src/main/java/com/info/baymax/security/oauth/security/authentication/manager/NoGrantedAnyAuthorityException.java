package com.info.baymax.security.oauth.security.authentication.manager;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * 说明：没有授予任何权限. <br>
 *
 * @author jingwei.yang
 * @date 2017年11月14日 下午2:31:26
 */
public class NoGrantedAnyAuthorityException extends AuthenticationServiceException {
    private static final long serialVersionUID = 5869204524357172888L;

    public NoGrantedAnyAuthorityException(String msg) {
        super(msg);
    }

    public NoGrantedAnyAuthorityException(String msg, Throwable t) {
        super(msg, t);
    }
}
