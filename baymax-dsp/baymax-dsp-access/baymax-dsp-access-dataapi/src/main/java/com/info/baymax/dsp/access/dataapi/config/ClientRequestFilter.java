package com.info.baymax.dsp.access.dataapi.config;

import com.info.baymax.dsp.access.dataapi.utils.IPAddressUtils;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @ClassName ClientRequestFilter
 * @Deacription 客户端请求过滤器
 * @Author Administrator
 * @Date 2021/3/24 14:58
 * @Version 1.0
 **/
@Slf4j
@Component
public class ClientRequestFilter implements WebFilter {

    @Autowired
    private DataCustAppService dataCustAppService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String ipAddress = IPAddressUtils.getIpAddress(request);

        String path = request.getURI().getPath();
        if (
                path.startsWith("/api/dsp/dataapi/data/pullRecords")
                || path.startsWith("/api/dsp/dataapi/data/pullAggs")
//                || path.startsWith("/api/dsp/dataapi/data/pullBySql")
//                || path.startsWith("/api/dsp/dataapi/data/secertkey")
                || path.startsWith("/api/dsp/dataapi/data/pullBySqlRemote")
        ) {
            // 处理请求主机信息
            HttpHeaders headers = request.getHeaders();
            List<String> userIds = headers.get("userId");
            String userId = null;
            if (userIds != null && !userIds.isEmpty()) {
                userId = userIds.get(0);
            }
            List<String> ipList = dataCustAppService.selectAccessIps(userId);
            if(!ipList.contains(ipAddress)){
                throw new RuntimeException("Wrong accessIp");
            }
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }
}
