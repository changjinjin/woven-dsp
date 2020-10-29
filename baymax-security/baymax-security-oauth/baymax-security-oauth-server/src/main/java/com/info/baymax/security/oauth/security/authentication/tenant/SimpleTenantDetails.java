package com.info.baymax.security.oauth.security.authentication.tenant;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端实体简单实现
 *
 * @author: jingwei.yang
 * @date: 2019年4月22日 下午2:05:31
 */
@Getter
@Setter
public class SimpleTenantDetails implements TenantDetails<Long> {

    /**
     * 客户端ID
     */
    private Long tenantId;

    /**
     * 租户名称
     */
    private String tenant;

    /**
     * 系统版本号
     */
    private String version;

    public SimpleTenantDetails() {
    }

    public SimpleTenantDetails(Long tenantId, String tenant, String version) {
        super();
        this.tenantId = tenantId;
        this.tenant = tenant;
        this.version = version;
    }

}
