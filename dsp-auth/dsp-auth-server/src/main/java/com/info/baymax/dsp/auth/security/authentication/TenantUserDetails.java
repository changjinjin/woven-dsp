package com.info.baymax.dsp.auth.security.authentication;

import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public abstract class TenantUserDetails extends User {
    private static final long serialVersionUID = 8805645346843532535L;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 租户
     */
    private Tenant tenant;

    public TenantUserDetails(String clientId, Tenant tenant, String username, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
        this.clientId = clientId;
        this.tenant = tenant;
    }

    public TenantUserDetails(String clientId, Tenant tenant, String username, String password, boolean enabled,
                             boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.clientId = clientId;
        this.tenant = tenant;
    }

    public static List<? extends GrantedAuthority> grantedAuthorities(Collection<String> authorities) {
        if (ICollections.hasNoElements(authorities)) {
            return null;
        }
        return authorities.stream().map(t -> new SimpleGrantedAuthority(t)).collect(Collectors.toList());
    }
}
