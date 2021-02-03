package com.info.baymax.security.oauth.security.authentication.customer;

import com.info.baymax.common.core.enums.types.YesNoType;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.security.oauth.security.authentication.DefaultGrantedAuthority;
import com.info.baymax.security.oauth.security.authentication.TenantUserDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
public class SimpleCustomerDetails extends TenantUserDetails {
    private static final long serialVersionUID = 8805645346843532535L;

    /**
     * 系统用户信息
     */
    private final Customer user;

    public SimpleCustomerDetails(String clientId, Tenant tenant, Customer user) {
        super(clientId, tenant, user.getUsername(), user.getPassword(), YesNoType.YES.equalsTo(user.getEnabled()), true,
            true, true, Arrays.asList(new DefaultGrantedAuthority("ROLE_CUSTOMER")));
        this.user = user;
    }
}
