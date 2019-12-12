package com.merce.woven.dsp.auth.security.exceptions;

/**
 * client密码错误
 * @author jingwei.yang
 * @date 2019年6月18日 上午10:33:55
 */
public class ClientBadSecretException extends ClientException {
    private static final long serialVersionUID = -2711776825981689462L;

    public ClientBadSecretException(String msg) {
        super(msg);
    }

    public ClientBadSecretException(String msg, Throwable t) {
        super(msg, t);
    }

    public ClientBadSecretException(String msg, String clientId) {
        super(msg, clientId);
    }

    public ClientBadSecretException(String msg, String clientId, Throwable t) {
        super(msg, clientId, t);
    }
}
