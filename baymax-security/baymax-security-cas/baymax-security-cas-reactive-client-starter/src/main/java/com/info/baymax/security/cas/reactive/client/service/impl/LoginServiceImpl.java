package com.info.baymax.security.cas.reactive.client.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.info.baymax.security.cas.reactive.client.config.CasServiceProperties;
import com.info.baymax.security.cas.reactive.client.config.HttpsRestTemplateFactory;
import com.info.baymax.security.cas.reactive.client.service.LoginService;
import com.info.baymax.security.cas.reactive.client.vo.LoginParamDto;
import com.info.baymax.security.cas.reactive.client.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@SuppressWarnings("unused")
public class LoginServiceImpl implements LoginService {
    private static final int RETRY_COUNT = 3;
    private static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    private final RestTemplate restTemplate = HttpsRestTemplateFactory.getInstance();
    @Autowired
    private CasServiceProperties casProperties;

    @Override
    public LoginVo restLogin(final LoginParamDto dto, ServerWebExchange exchange) {
        final String server = casProperties.getServer() + "/v1/tickets";
        for (int i = 0; i < RETRY_COUNT; ++i) {
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
            httpHeaders.add("CLIENT_IP", getClientIp(exchange.getRequest()));
            final String body = "username=" + dto.getUsername() + "&password=" + dto.getPassword() + "&" + "Time" + "="
                + dto.getTime();
            final HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
            try {
                final ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(server, entity,
                    String.class, new Object[0]);
                final String responseBody = responseEntity.getBody();
                switch (responseEntity.getStatusCodeValue()) {
                    case 201: {
                        final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(responseBody);
                        if (matcher.matches()) {
                            return this.createSuccessVo(server, matcher.group(1), exchange.getResponse(),
                                casProperties.getBaseUrl(), casProperties.getService());
                        }
                        log.warn("Successful ticket granting request, but no ticket found!");
                        log.info("Response (1k): " + responseBody.substring(0, Math.min(1024, responseBody.length())));
                        break;
                    }
                    case 200: {
                        final JSONObject responseJson = JSONUtil.parseObj(responseBody);
                        final Integer errorCode = responseJson.get("ErrorCode", Integer.class);
                        final String errorMessage = responseJson.get("ErrorMessage", String.class);
                        if (ObjectUtil.isNotNull(errorCode) && ObjectUtil.isNotNull(errorMessage)) {
                            return this.createFailVo(errorCode, errorMessage);
                        }
                        break;
                    }
                    default: {
                        log.warn("Invalid response code " + responseEntity.getStatusCodeValue() + "from CAS server!");
                        log.info("Response: " + responseBody);
                        break;
                    }
                }
            } catch (Exception exception) {
                log.error("无法从CAS-Server请求获取TicketGrantingTicket的票据,请确认CAS-Server应用的状态", exception);
            }
        }
        return this.createFailVo();
    }

    @Override
    public String loginOut(ServerWebExchange exchange) {
        final String server = casProperties.getServer() + "/logout";
        for (int i = 0; i < 3; ++i) {
            try {
                final HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
                final HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
                final ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(server, entity,
                    String.class, new Object[0]);
                final String responseBody = responseEntity.getBody();
                if (responseEntity.getStatusCodeValue() == 200) {
                    log.info("cas logout success");
                    return "success";
                }
            } catch (Exception e) {
                log.error("cas logout exception: {}", e.toString());
            }
        }
        return "error";
    }

    public static String getClientIp(final ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getHostString();
        }
        return ip;
    }

    private LoginVo createFailVo() {
        final LoginVo vo = new LoginVo();
        vo.setOpCode(101);
        vo.setOpDesc("用户登录失败，请确认账号密码是否正确、用户是否限制ip登录或者是否超过有效期！");
        return vo;
    }

    private LoginVo createFailVo(Integer errorCode, String errorMessage) {
        final LoginVo vo = new LoginVo();
        vo.setOpCode(errorCode);
        vo.setOpDesc(errorMessage);
        return vo;
    }

    private LoginVo createSuccessVo(String server, String ticketGrantingTicket, ServerHttpResponse response,
                                    String indexUrl, String service) {
        final LoginVo vo = new LoginVo();
        final String redirectUrl = server + "/" + ticketGrantingTicket;
        vo.setTgTicket(ticketGrantingTicket);
        vo.setTgtUrl(redirectUrl);
        vo.setIndexUrl(indexUrl);
        vo.setService(service);
        vo.setOpCode(0);
        vo.setOpDesc("Success");
        response.addCookie(
            ResponseCookie.from("authenticated", "1").maxAge(Duration.ofSeconds(1800)).path("/").build());
        return vo;
    }
}
