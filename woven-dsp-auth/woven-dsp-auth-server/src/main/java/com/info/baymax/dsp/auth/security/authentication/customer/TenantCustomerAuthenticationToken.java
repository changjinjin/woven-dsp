package com.info.baymax.dsp.auth.security.authentication.customer;

import com.info.baymax.dsp.auth.security.authentication.AbstractTenantUserAuthenticationToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
public class TenantCustomerAuthenticationToken extends AbstractTenantUserAuthenticationToken {
    private static final long serialVersionUID = -9048803099293495803L;

    public TenantCustomerAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
                                             String version) {
        super(principal, credentials, clientId, tenant, version);
    }

    public TenantCustomerAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
                                             String version, Object tenantId, Object userId, boolean admin,
                                             Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, clientId, tenant, version, tenantId, userId, admin, authorities);
    }

    @Override
    public UserType getUserType() {
        return UserType.Manager;
    }
}
