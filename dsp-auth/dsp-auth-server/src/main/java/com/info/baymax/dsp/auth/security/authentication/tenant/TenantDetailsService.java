package com.info.baymax.dsp.auth.security.authentication.tenant;

/**
 * 客户端信息加载接口
 *
 * @author: jingwei.yang
 * @date: 2019年4月22日 下午2:01:57
 */
public interface TenantDetailsService {

    /**
     * Find {@link TenantDetails} by tenant name.
     *
     * @param tenantName the tenant name to look up
     * @return the {@link TenantDetails}. Cannot be null
     */
    TenantDetails<?> findByTenant(String tenantName);

}
