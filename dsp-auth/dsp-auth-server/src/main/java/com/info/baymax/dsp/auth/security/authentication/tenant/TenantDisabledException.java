package com.info.baymax.dsp.auth.security.authentication.tenant;

/**
 * 说明： clientId错误. <br>
 *
 * @author jingwei.yang
 * @date 2017年11月14日 下午1:45:42
 */
public class TenantDisabledException extends TenantException {
    private static final long serialVersionUID = -2711776825981689462L;

    public TenantDisabledException(String msg) {
        super(msg);
    }

    public TenantDisabledException(String msg, Throwable t) {
        super(msg, t);
    }

    public TenantDisabledException(String msg, String clientId) {
        super(msg, clientId);
    }

    public TenantDisabledException(String msg, String clientId, Throwable t) {
        super(msg, clientId, t);
    }
}
