package com.info.baymax.security.oauth.security.authentication.manager;

import com.info.baymax.common.core.enums.types.YesNoType;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.validation.passay.PwdInfo;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.security.oauth.security.authentication.TenantUserDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class SimpleManagerDetails extends TenantUserDetails {
    private static final long serialVersionUID = 8805645346843532535L;

    /**
     * 系统用户信息
     */
    private final User user;

    /**
     * 密码信息
     */
    public PwdInfo pwdInfo;

    public SimpleManagerDetails(String clientId, Tenant tenant, User user) {
        super(clientId, tenant, user.getUsername(), user.getPassword(), YesNoType.YES.equalsTo(user.getEnabled()),
            user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),
            user.getAuthorities());
        this.user = user;
    }

    public static List<? extends GrantedAuthority> grantedAuthorities(Collection<String> authorities) {
        if (ICollections.hasNoElements(authorities)) {
            return null;
        }
        return authorities.stream().map(t -> new SimpleGrantedAuthority(t)).collect(Collectors.toList());
    }
}
