package com.info.baymax.security.oauth.security.authentication;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.info.baymax.security.oauth.api.UserInfo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractTenantUserAuthenticationToken extends UsernamePasswordAuthenticationToken
    implements UserInfo {
    private static final long serialVersionUID = 7444831017334725302L;
    private String clientId;
    private String tenant;
    private String version;

    private String userId;
    private String tenantId;
    private boolean admin;

    public AbstractTenantUserAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
                                                 String version) {
        super(principal, credentials);
        this.clientId = clientId;
        this.tenant = tenant;
        this.version = version;
    }

    public AbstractTenantUserAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
                                                 String version, String tenantId, String userId, boolean admin,
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
