package com.info.baymax.dsp.auth.security.authentication.tenant;

import org.springframework.security.core.AuthenticationException;

/**
 * 说明： client异常. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年11月14日 下午1:45:42
 */
public class TenantException extends AuthenticationException {
    private static final long serialVersionUID = -2711776825981689462L;

    private String clientId;

    public TenantException(String msg) {
        super(msg);
    }

    public TenantException(String msg, Throwable t) {
        super(msg, t);
    }

    public TenantException(String msg, String clientId) {
        this(msg);
        this.clientId = clientId;
    }

    public TenantException(String msg, String clientId, Throwable t) {
        super(msg, t);
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }
}
