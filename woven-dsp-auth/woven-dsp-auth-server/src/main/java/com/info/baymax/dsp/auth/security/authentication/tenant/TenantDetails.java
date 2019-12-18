package com.info.baymax.dsp.auth.security.authentication.tenant;

import java.io.Serializable;

/**
 * 客户端信息抽象信息
 *
 * @author: jingwei.yang
 * @date: 2019年4月19日 上午10:30:46
 */
public interface TenantDetails<ID extends Serializable> {

    /**
     * 租户ID
     *
     * @return 租户ID
     */
    ID getTenantId();

    /**
     * 获取客户端ID
     *
     * @return 客户端ID
     */
    String getTenant();

    /**
     * 获取系统版本号
     *
     * @return 系统版本号
     */
    String getVersion();
}
