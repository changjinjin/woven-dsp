package com.info.baymax.security.oauth.security.authentication.license;

import com.info.baymax.security.oauth.security.authentication.tenant.TenantException;

public class LicenseNotActivatedException extends TenantException {
    private static final long serialVersionUID = -5416346683661102209L;

    public LicenseNotActivatedException(String msg) {
        super(msg);
    }

    public LicenseNotActivatedException(String msg, Throwable t) {
        super(msg, t);
    }

    public LicenseNotActivatedException(String msg, String clientId) {
        super(msg, clientId);
    }

    public LicenseNotActivatedException(String msg, String clientId, Throwable t) {
        super(msg, clientId, t);
    }
}
