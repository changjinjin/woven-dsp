package com.info.baymax.dsp.access.dataapi.config;

import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.utils.crypto.RSAGenerater;
import com.info.baymax.dsp.access.dataapi.utils.IPAddressUtils;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
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

import java.util.Arrays;
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

    private boolean ipAddressVerification(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ipAddress = IPAddressUtils.getIpAddress(request);

        List<String> accessKeys = headers.get("accessKey");
        if(null != accessKeys && accessKeys.size() > 0){
            String accessKey = accessKeys.get(0);
            log.info("accessKey = " + accessKey);
            DataCustApp dataCustApp = dataCustAppService.selectByAccessKeyNotNull(accessKey);
            String[] accessIps = dataCustApp.getAccessIp();
            List<String> ipList = Arrays.asList(accessIps);
            if (ipList.contains(ipAddress)) {
                return true;
            }else{
                return false;
            }
        }else {
            throw new RuntimeException("accessKey cannot be null");
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if(path.indexOf("pullBySqlRemote") != -1
                || path.indexOf("pullRecordsRemote") != -1
                || path.indexOf("pullAggsRemote") != -1){

            boolean flag = ipAddressVerification(request);
            if (true == flag) {
                boolean b = accessTokenVerification(request);
                if(b == true){
                    return chain.filter(exchange);
                }else{
                    throw new RuntimeException("Wrong accessToken");
                }
            } else {
                throw new RuntimeException("Wrong accessIp");
            }
        }else{
            return chain.filter(exchange);
        }
    }

    private boolean accessTokenVerification(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        List<String> accessKeys = headers.get("accessKey");
        List<String> accessTokens = headers.get("accessToken");
        if(ICollections.hasNoElements(accessKeys)){
            log.info("accessKey为空");
            throw new RuntimeException("accessKey cannot be null");
        }
        if(ICollections.hasNoElements(accessTokens)){
            log.info("accessToken为空");
            throw new RuntimeException("accessToken cannot be null");
        }
        String accessKey = accessKeys.get(0);
        String accessToken = accessTokens.get(0);
        DataCustApp app = dataCustAppService.selectByAccessKeyNotNull(accessKey);
        String generateRandomPassword = RSAGenerater.decryptByPublicKey(accessToken, app.getPublicKey());
        return generateRandomPassword.startsWith(accessKey);
    }
}
