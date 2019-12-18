package com.info.baymax.dsp.auth.security.exceptions;

/**
 * 说明： clientId错误. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年11月14日 下午1:45:42
 */
public class ClientIdDisabledException extends ClientException {
    private static final long serialVersionUID = -2711776825981689462L;

    public ClientIdDisabledException(String msg) {
        super(msg);
    }

    public ClientIdDisabledException(String msg, Throwable t) {
        super(msg, t);
    }

    public ClientIdDisabledException(String msg, String clientId) {
        super(msg, clientId);
    }

    public ClientIdDisabledException(String msg, String clientId, Throwable t) {
        super(msg, clientId, t);
    }
}
