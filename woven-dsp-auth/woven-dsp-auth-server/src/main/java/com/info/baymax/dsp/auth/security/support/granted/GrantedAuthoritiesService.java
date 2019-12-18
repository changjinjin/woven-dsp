package com.info.baymax.dsp.auth.security.support.token.granted;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 权限业务接口
 *
 * @author: jingwei.yang
 * @date: 2019年4月22日 上午10:32:29
 */
public interface GrantedAuthoritiesService {

    /**
     * 查询客户端所有的权限值列表
     *
     * @param clientId 客户端ID
     * @return 客户端所有的权限值
     */
    Collection<String> findGrantedAuthorityUrlsByClientId(String clientId);

    /**
     * 用户授予的权限列表
     *
     * @param clientId 客户端ID
     * @return 用户权限列表0000.
     */
    Collection<? extends GrantedAuthority> findGrantedAuthoritiesByClientId(String clientId);

    /**
     * 用户授予的权限值列表
     *
     * @param clientId 客户端ID
     * @param username 用户名
     * @return 用户权限值
     */
    Collection<String> findGrantedAuthorityUrlsByClientIdAndUsername(String clientId, String tenant, String username);

    /**
     * 用户授予的权限列表
     *
     * @param clientId 客户端ID
     * @param username 用户名
     * @return 用户权限列表
     */
    Collection<? extends GrantedAuthority> findGrantedAuthoritiesByClientIdAndUsername(String clientId, String tenant,
                                                                                       String username);

}
