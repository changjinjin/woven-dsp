package com.info.baymax.dsp.auth.security.authentication.tenant;

/**
 * 说明： clientId错误. <br>
 *
 * @author jingwei.yang
 * @date 2017年11月14日 下午1:45:42
 */
public class TenantNotFoundException extends TenantException {
    private static final long serialVersionUID = -2711776825981689462L;

    public TenantNotFoundException(String msg) {
        super(msg);
    }

    public TenantNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public TenantNotFoundException(String msg, String clientId) {
        super(msg, clientId);
    }

    public TenantNotFoundException(String msg, String clientId, Throwable t) {
        super(msg, clientId, t);
    }
}
