package com.info.baymax.dsp.auth.api;

public interface UserInfo {

    public String getClientId();

    public Object getTenantId();

    public String getTenantName();

    public Object getUserId();

    public String getUserName();

    public boolean isAdmin();

    public String getVersion();

}
