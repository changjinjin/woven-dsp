package com.info.baymax.security.oauth.api;

public interface UserInfo {

    /**
     * 获取客户端ID
     */
    String getClientId();

    /**
     * 获取租户ID
     */
    String getTenantId();

    /**
     * 获取租户名称
     */
    String getTenantName();

    /**
     * 获取用户ID
     */
    UserType getUserType();

    /**
     * 获取用户ID
     */
    String getUserId();

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

    public enum UserType {
        Manager, Customer;
    }

}
