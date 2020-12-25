package com.info.baymax.dsp.gateway.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

/**
 * 请求体中信息拿出后放入缓存中以便于后续业务使用，解决“Only one connection receive subscriber allowed.”问题
 *
 * @author jingwei.yang
 * @date 2019年11月11日 上午10:35:08
 */
@Component
@Slf4j
public class HttpCachedBodyGlobalFilter implements GlobalFilter, Ordered {

    public static final String CACHED_REQUEST_BODY_KEY = "cachedBody";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();
        String contentType = request.getHeaders().getFirst("Content-Type");
        if ("POST".equals(method) && contentType != null && !contentType.startsWith("multipart/form-data")) {
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                try {
                    String bodyString = new String(bytes, "utf-8");
                    exchange.getAttributes().put(CACHED_REQUEST_BODY_KEY, bodyString);
                    if (log.isTraceEnabled()) {
                        log.trace("request body:" + bodyString);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                DataBufferUtils.release(dataBuffer);
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return Mono.just(buffer);
                });

                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}