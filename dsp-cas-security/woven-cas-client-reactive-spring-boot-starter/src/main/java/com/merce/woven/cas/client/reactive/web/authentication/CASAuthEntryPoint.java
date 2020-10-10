package com.merce.woven.cas.client.reactive.web.authentication;

import cn.hutool.json.JSONObject;
import com.merce.woven.cas.client.reactive.config.CasServiceProperties;
import com.merce.woven.cas.client.reactive.util.CASAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.cas.reactive.utils.CommonUtils;
import org.springframework.security.cas.reactive.web.CasServerAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Slf4j
public class CASAuthEntryPoint extends CasServerAuthenticationEntryPoint {
    private final CasServiceProperties casServiceProperties;

    public CASAuthEntryPoint(CasServiceProperties casServiceProperties) {
        this.casServiceProperties = casServiceProperties;
        this.setServiceProperties(casServiceProperties);
        this.setLoginUrl(casServiceProperties.getServer()+"/login");
    }

    protected String createServiceUrl(ServerWebExchange exchange, String loginPath) {
        this.getServiceProperties().setService(casServiceProperties.getService());
        return super.createServiceUrl(exchange);
    }

    @Override
    protected void preCommence(ServerWebExchange exchange) {
        final boolean isAjaxRequest = CASAuthUtil.isAjaxRequest(exchange);
        final boolean isAuthenticated = CASAuthUtil.hasAuthenticatedCookie(exchange, false);
        ServerHttpResponse response = exchange.getResponse();
        if (isAjaxRequest && !isAuthenticated) {
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
            try {
                final String urlEncodedService = this.createServiceUrl(exchange, getLoginUrl());
                final String redirectUrl = this.createRedirectUrl(urlEncodedService);
                DataBuffer buffer = response.bufferFactory().wrap(
                    CASAuthUtil.buildUnAuthenticatedJson(exchange, redirectUrl).getBytes(Charset.defaultCharset()));
                response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static boolean isCheckLoginRequest(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        return path.endsWith("login_check");
    }

    public static boolean isFirstPageRequest(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        return path.endsWith("loadMenus");
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authenticationException) {
        ServerHttpResponse response = exchange.getResponse();
        final boolean isAjaxRequest = CASAuthUtil.isAjaxRequest(exchange);
        final boolean isAuthenticated = CASAuthUtil.hasAuthenticatedCookie(exchange, false);

        log.info("==Request {}, isAjaxRequest: {}, isAuthenticated {}" ,exchange.getRequest().getPath(), Boolean.toString(isAjaxRequest), Boolean.toString(isAuthenticated));

        if("remote".equalsIgnoreCase(casServiceProperties.getMode())) {
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            try {
                boolean encodeServiceUrlWithSessionId = true;
                String serviceUrl = CommonUtils.constructServiceUrl(exchange, casServiceProperties.getFirstPage(), null,
                        casServiceProperties.getArtifactParameter(), encodeServiceUrlWithSessionId);
                String loginUrl = CommonUtils.constructServiceUrl(exchange, casServiceProperties.getPlatformServer() + "/login", null,
                        casServiceProperties.getArtifactParameter(), encodeServiceUrlWithSessionId);
                String redirectUrl = casServiceProperties.getServer() + "/login?service=" + serviceUrl + "&loginUrl=" + loginUrl;

                JSONObject map = new JSONObject();
                map.put("status", 200);
                map.put("mode", casServiceProperties.getMode());
                if (isCheckLoginRequest(exchange)) {
                    map.put("message", "未登录，请登录。");
                }else{
                    map.put("message", "同步登录信息。");
                }
                map.put("redirectUrl", redirectUrl);
                String stringPretty = map.toStringPretty();
                DataBuffer buffer = response.bufferFactory().wrap(stringPretty.getBytes(Charset.defaultCharset()));
                return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
            } catch (Exception e) {
                log.error("loadMenus: " + e.getMessage(), e);
            }
        }

        //local 模式
        if (isCheckLoginRequest(exchange)) {
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            try {
                boolean encodeServiceUrlWithSessionId = true;
                String serviceUrl = CommonUtils.constructServiceUrl(exchange, casServiceProperties.getService(), null,
                        casServiceProperties.getArtifactParameter(), encodeServiceUrlWithSessionId);

                JSONObject map = new JSONObject();
                map.put("status", 1);
                map.put("message", "未登录，请登录。");
                map.put("mode", casServiceProperties.getMode());
                map.put("loginUrl", casServiceProperties.getPlatformServer() + "/login");
                map.put("redirectUrl", casServiceProperties.getPlatformServer() + "/login?service="+serviceUrl);
                String stringPretty = map.toStringPretty();
                DataBuffer buffer = response.bufferFactory().wrap(stringPretty.getBytes(Charset.defaultCharset()));
                return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
            } catch (Exception e) {
                log.error("loginCheck: " +e.getMessage(), e);
            }
        }


        return super.commence(exchange, authenticationException);
    }

    protected String createRedirectUrl(String serviceUrl, String loginUrl) {
        String url = super.createRedirectUrl(serviceUrl);
        String encodeLoginUrl = "";
        if (!StringUtils.isEmpty(loginUrl)) {
            url += (url.contains("?") ? "&" : "?");
            if (!loginUrl.startsWith("http://") && !loginUrl.startsWith("https://")) {
                final String appClient = this.getDomain(serviceUrl);
                encodeLoginUrl = appClient + loginUrl;
            } else {
                encodeLoginUrl = loginUrl;
            }
            url = url + "loginUrl=" + encodeLoginUrl;
        }
        return url;
    }

    public String getDomain(final String url) {
        if (url == null) {
            return null;
        }
        int l = 0;
        if (url.startsWith("http://")) {
            l = "http://".length();
        } else if (url.startsWith("https://")) {
            l = "https://".length();
        }
        final int slash = url.indexOf(47, l);
        String domain;
        if (slash > 0) {
            domain = url.substring(0, slash);
        } else {
            domain = url;
        }
        return domain;
    }
}
