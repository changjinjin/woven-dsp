package com.info.baymax.dsp.gateway.web.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.gateway.feign.RestOperationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.result.condition.RequestCondition;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * 服务启动时加载URI映射信息到内存中以便于访问时使用,主要用于根据请求路径从swagger文档中解析操作的名称
 *
 * @author jingwei.yang
 * @date 2019年11月5日 下午12:34:01
 */
@Component
@Slf4j
public class RequestUriMappingsHolder {

    private final RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
    private List<RestOperation> restOperationBeans = Lists.newArrayList();
    private final Map<String, RestOperation> uriMappings = new HashMap<>();
    private final Map<String, RestOperation> enabledUriMappings = new HashMap<>();
    private final Map<String, RequestMappingInfo> requestMappingInfos = new HashMap<>();
    private boolean initialized = false;

    @Autowired
    private RestOperationClient restOperationClient;

    @PostConstruct
    public void init() {
        doInit();
    }

    private void doInit() {
        try {
            initRestOperationBeans();
            resolveUriMappings();
            resolveEnabledUriMappings();
            resolveRequestMappingInfos();
        } catch (Exception e) {
            initialized = false;
            log.error("init uri mappings failed.", e);
        }
    }

    private void initRestOperationBeans() {
        if (restOperationBeans.isEmpty()) {
            List<JSONObject> list = null;
            try {
                list = restOperationClient.fetchRestOperations(new RestOperation());
                if (ICollections.hasElements(list)) {
                    restOperationBeans = JSON.parseArray(JSON.toJSONString(list), RestOperation.class);
                    initialized = true;
                } else {
                    initialized = false;
                    log.warn("fetch rest operations failed, may be other services is disabled yet.");
                }
            } catch (Exception e) {
                initialized = false;
                log.error("fetch rest operations error", e);
            }
        }
    }

    /**
     * 解析文档内容到uriMappings和requestMappingInfos中
     */
    private void resolveUriMappings() {
        if (ICollections.hasElements(restOperationBeans)) {
            for (RestOperation pathPatternBean : restOperationBeans) {
                uriMappings.put(pathPatternBean.operationKey(), pathPatternBean);
            }
        }
    }

    private void resolveEnabledUriMappings() throws IOException {
        if (ICollections.hasElements(restOperationBeans)) {
            for (RestOperation pathPatternBean : restOperationBeans) {
                if (pathPatternBean.getEnabled() != null && pathPatternBean.getEnabled()) {
                    enabledUriMappings.put(pathPatternBean.operationKey(), pathPatternBean);
                }
            }
        }
    }

    /**
     * 将文档解析成RequestMappingInfo映射
     */
    private void resolveRequestMappingInfos() {
        if (uriMappings != null && !uriMappings.isEmpty()) {
            Set<String> keySet = uriMappings.keySet();
            keySet.forEach(key -> {
                requestMappingInfos.put(key, createRequestMappingInfo(uriMappings.get(key), null));
            });
        }
    }

    /**
     * 根据文档中解析的接口属性信息创建RequestMappingInfo
     *
     * @param requestMapping  根据接口文档解析的接口属性包装对象
     * @param customCondition 其他自定义配置信息
     * @return 根据文档生成的RequestMappingInfo
     */
    private RequestMappingInfo createRequestMappingInfo(RestOperation requestMapping,
                                                        RequestCondition<?> customCondition) {
        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(new String[]{requestMapping.getFullPath()})
            .methods(new RequestMethod[]{RequestMethod.valueOf(requestMapping.getMethod().toUpperCase())})
            // .params(requestMapping.params()).headers(requestMapping.headers())
            .consumes(requestMapping.getConsumes()).produces(requestMapping.getProduces())
            .mappingName(requestMapping.operationKey());
        if (customCondition != null) {
            builder.customCondition(customCondition);
        }
        return builder.options(this.config).build();
    }

    public RestOperation getOperation(ServerWebExchange exchange) {
        if (!initialized) {
            doInit();
        }
        ServerHttpRequest request = exchange.getRequest();
        String methodValue = request.getMethodValue();

        // 先想根据pathPattern从缓存中拿
        PathPattern pathPattern = exchange.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (pathPattern != null) {
            String patternString = pathPattern.getPatternString();
            RestOperation operation = getOperation(patternString, methodValue);
            if (operation != null) {
                return operation;
            }
        }

        RestOperation operation = getOperation(request.getPath().value(), methodValue);
        if (operation != null) {
            return operation;
        }

        // 上面的都匹配不到则根据RequestMapping去匹配
        return getOperationByPathPatterns(exchange);
    }

    private RestOperation getOperation(String path, String method) {
        return uriMappings.get(operationKey(path, method));
    }

    private RestOperation getOperationByPathPatterns(ServerWebExchange exchange) {
        if (requestMappingInfos != null && !requestMappingInfos.isEmpty()) {
            Iterator<String> iterator = requestMappingInfos.keySet().iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                RequestMappingInfo matchingCondition = requestMappingInfos.get(next).getMatchingCondition(exchange);
                if (matchingCondition != null) {
                    return uriMappings.get(next);
                }
            }
        }
        return null;
    }

    public static String operationKey(String path, String method) {
        return path + "@" + method.toUpperCase();
    }

    public boolean contains(String path, String method) {
        return enabledUriMappings.containsKey(operationKey(path, method));
    }
}
