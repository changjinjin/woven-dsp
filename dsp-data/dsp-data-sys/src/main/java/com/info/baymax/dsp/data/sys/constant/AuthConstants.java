package com.info.baymax.dsp.data.sys.constant;

public class AuthConstants {
    public static final String TOKEN_HEADER_PARAM = "Authorization";// token参数名
    public static final String TOKEN_ENTRY_POINT = "/oauth/token";// 令牌端点，获取token接口
    public static final String TOKEN_AUTHORIZE_ENTRY_POINT = "/oauth/authorize";// 授权端点，CODE方式认证接口
    public static final String TOKEN_CHECK_ENTRY_POINT = "/oauth/check_token";// 检查token接口，用于资源服务访问的令牌解析端点,资源服务器用来校验token
    public static final String TOKEN_KEY_ENTRY_POINT = "/oauth/token_key";// 提供公有密匙的端点，如果你使用JWT令牌的话
    public static final String TOKEN_CONFIRM_ACCESS_ENTRY_POINT = "/oauth/confirm_access";// 用户确认授权提交端点
    public static final String TOKEN_ERROR_ENTRY_POINT = "oauth/error";// 授权服务错误信息端点
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/oauth/refresh_token";// 刷新token接口
    public static final String TOKEN_REVOKE_ENTRY_POINT = "/oauth/revoke_token";// 注销token接口
    public static final String TOKEN_AUTH_ENTRY_POINT = "/api/**";// 需要鉴权的路径

    /**
     * 用户信息头
     */
    public static final String USER_INFO = "user_info";

    /**
     * 用户信息头
     */
    public static final String USER_HEADER = "x-user-header";

    /**
     * 角色信息头
     */
    public static final String ROLE_HEADER = "x-role-header";

    /**
     * 版本定义
     */
    public static final String VERSION = "version";
}
