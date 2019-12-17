package com.info.baymax.dsp.auth.oauth2.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.info.baymax.dsp.auth.api.UserInfo;

import java.util.Collection;

@Setter
@Getter
public class TenantUserAuthenticationToken extends UsernamePasswordAuthenticationToken implements UserInfo {
    private static final long serialVersionUID = 7444831017334725302L;
    private String clientId;
    private String tenant;
    private String version;

    private Object userId;
    private Object tenantId;
    private boolean admin;

    public TenantUserAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
                                         String version) {
        super(principal, credentials);
        this.clientId = clientId;
        this.tenant = tenant;
        this.version = version;
    }

    public TenantUserAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
                                         String version, Object tenantId, Object userId, boolean admin,
                                         Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.clientId = clientId;
        this.tenantId = tenantId;
        this.tenant = tenant;
        this.userId = userId;
        this.version = version;
        this.admin = admin;
    }

    @Override
    public String getTenantName() {
        return tenant;
    }

    @Override
    public String getUserName() {
        return super.getPrincipal().toString();
    }
}