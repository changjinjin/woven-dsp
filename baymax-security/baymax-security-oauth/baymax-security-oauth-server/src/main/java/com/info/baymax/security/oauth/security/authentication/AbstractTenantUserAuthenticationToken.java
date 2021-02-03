package com.info.baymax.security.oauth.security.authentication;

import com.info.baymax.security.oauth.api.UserInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
public abstract class AbstractTenantUserAuthenticationToken extends AbstractAuthenticationToken implements UserInfo {
    private static final long serialVersionUID = 7444831017334725302L;
    // ~ Instance fields
    // ================================================================================================

    private final Object principal;
    private Object credentials;

    private String clientId;
    private String tenant;
    private String version;

    private String userId;
    private String tenantId;
    private boolean admin;

    public AbstractTenantUserAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
                                                 String version) {
        super(null);
        super.setAuthenticated(false);
        this.principal = principal;
        this.credentials = credentials;
        this.clientId = clientId;
        this.tenant = tenant;
        this.version = version;
    }

    public AbstractTenantUserAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
                                                 String version, String tenantId, String userId, boolean admin,
                                                 Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
        this.clientId = clientId;
        this.tenantId = tenantId;
        this.tenant = tenant;
        this.userId = userId;
        this.version = version;
        this.admin = admin;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }

    @Override
    public String getTenantName() {
        return tenant;
    }

    @Override
    public String getUserName() {
        return getPrincipal().toString();
    }
}
