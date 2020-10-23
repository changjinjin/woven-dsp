package com.info.baymax.dsp.gateway.filter;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.*;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.dsp.data.sys.service.security.UserService;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.common.queryapi.result.ErrType;
import com.info.baymax.common.queryapi.result.Response;
/**
 * 处理当前租户和登录用户信息到上下文中
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午2:40:24
 */
@Component
public class SaasContextHandler {

    private static Logger logger = LoggerFactory.getLogger(SaasContextHandler.class);

    @Autowired
    private TenantService tenantService;
    @Autowired
    private UserService merceUserService;

    public void handle(ServerWebExchange exchange, CasAuthenticationToken authentication) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS");

        // 跳过非/api开头的请求
        String path = request.getURI().getPath();
        if (!path.startsWith("/api/")) {
            return;
        }

        SaasContext ctx = SaasContext.getCurrentSaasContext();

        // 处理请求主机信息
        HttpHeaders headers = request.getHeaders();
        List<String> hosts = headers.get("Host");
        String host = null;
        if (ICollections.hasElements(hosts)) {
            host = hosts.get(0);
        }
        host = host.split(":")[0];

        List<String> ips = headers.get("X-Forwarded-For");
        String clientIp = null;
        if (ICollections.hasElements(ips)) {
            clientIp = ips.get(0);
        } else {
            clientIp = request.getRemoteAddress().getHostName();
        }
        ctx.setHost(host);
        ctx.setClientId(clientIp);

        // 处理租户信息和用户信息
        if (authentication != null) {
            // ClientDetails clientDetails = authentication.getClientDetails();
            String clientId = "default";

            // 设置租户信息
            Tenant tenant = tenantService.findByName(clientId);
            if (tenant == null) {
                response.setStatusCode(HttpStatus.OK);
                response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                DataBuffer buffer = response.bufferFactory()
                        .wrap(JsonUtils.toJson(Response.ok(ErrType.UNAUTHORIZED)).getBytes(Charset.defaultCharset()));
                response.writeWith(reactor.core.publisher.Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
            }

            if (logger.isTraceEnabled()) {
                logger.trace("{} : set tenant {} ", request.getURI(), JsonUtils.toJson(tenant));
            }

            // 设置用户信息
            User user = merceUserService.findByTenantAndUsername(tenant.getId(), authentication.getName());
            if (user == null) {
                response.setStatusCode(HttpStatus.OK);
                response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                DataBuffer buffer = response.bufferFactory()
                        .wrap(JsonUtils.toJson(Response.ok(ErrType.UNAUTHORIZED)).getBytes(Charset.defaultCharset()));
                response.writeWith(reactor.core.publisher.Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
            }
            if (logger.isTraceEnabled()) {
                logger.trace("{} : set user {} ", request.getURI(), JsonUtils.toJson(user));
            }
            ctx.setTenantId(tenant.getId());
            ctx.setTenantName(tenant.getName());
            ctx.setUserId(user.getId());
            ctx.setUsername(user.getLoginId());
        }
    }

}
