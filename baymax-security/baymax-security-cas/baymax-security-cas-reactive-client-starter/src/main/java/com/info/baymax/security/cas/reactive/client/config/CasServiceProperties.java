package com.info.baymax.security.cas.reactive.client.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.cas.ServiceProperties;

@Data
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "security.cas")
public class CasServiceProperties extends ServiceProperties {
    private static final String defaultRootUrl = "http://localhost:8080/";
    private static final String defaultMode = "local";

    /**
     * 可以跳转到本地登录页也可以跳转到远端cas登录页,支持配置local/remote
     */
    private String mode = defaultMode;
    /**
     * cas server 地址
     */
    private String server = concatAndTrimUrl(defaultRootUrl, "cas");

    /**
     * 数据中台地址
     */
    private String platformServer;

    /**
     * baymax index 地址
     */
    private String baseUrl = defaultRootUrl;

    /**
     * baymax系统首页
     */
    private String firstPage;

    /**
     * login 路径
     */
    private String loginPath = "/cas/login";

    /**
     * 验证是否登陆并跳转到service的方法
     */
    private String serviceCheckSuffix = "/login"; // /login  /check

    /**
     * login_check返回的redirectUrl需要前端跳转，跳转后是cas帮着跳转到资源页（true）还是需要前端接受响应再做跳转（false）
     */
    private String serviceAutoAccess = "true";

    /**
     * logout 路径
     */
    private String logoutPath = "/cas/logout";

    private String ticketValidateClass ;//Saml11TicketValidator, Cas30ProxyReceivingTicketValidationFilter
    private String ticketValidateAddress; //http://10.40.3.29:8080/cas

    /**
     * 访问白名单
     */
    private String[] whiteList = new String[]{"/", "/**.html", "/**.js", "/**.css", "/**.ico",};

    private String concatAndTrimUrl(String baseUrl, String path) {
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return baseUrl + path;
    }
}
