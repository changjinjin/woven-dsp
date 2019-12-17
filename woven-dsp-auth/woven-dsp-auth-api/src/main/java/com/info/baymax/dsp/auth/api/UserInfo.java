package com.info.baymax.dsp.auth.api;

public interface UserInfo {

    /**
     * 获取客户端ID
     */
    String getClientId();

    /**
     * 获取租户ID
     */
    Object getTenantId();

    /**
     * 获取租户名称
     */
    String getTenantName();

    /**
     * 获取用户ID
     */
    Object getUserId();

    /**
     * 获取用户名称
     */
    String getUserName();

    /**
     * 用户是否是超级管理员
     */
    boolean isAdmin();

    /**
     * 获取系统版本号
     */
    String getVersion();

}
