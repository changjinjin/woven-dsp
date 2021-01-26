package com.info.baymax.common.webflux.server.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * 响应状态码抉择器
 *
 * @author jingwei.yang
 * @date 2021年1月26日 下午5:33:35
 */
public interface HttpStatusDeterminer {

    /**
     * 根据response或者error 决定响应的 HttpStatus
     *
     * @param response 原始响应对象
     * @param error    异常对象
     * @return 决策后的响应码
     */
    HttpStatus determineHttpStatus(ServerHttpResponse response, Throwable error);

}
