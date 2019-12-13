package com.info.baymax.common.comp.serialize.jackson.fieldFilter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.comp.serialize.annotation.JsonBody;
import com.info.baymax.common.comp.serialize.annotation.JsonBodys;
import com.info.baymax.common.message.exception.BizException;
import com.info.baymax.common.message.result.ErrType;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.AbstractMessageWriterResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public class ServerFilterFieldsHandlerResultHandler extends AbstractMessageWriterResultHandler
    implements HandlerResultHandler {

    public ServerFilterFieldsHandlerResultHandler(List<HttpMessageWriter<?>> writers,
                                                  RequestedContentTypeResolver resolver) {
        this(writers, resolver, ReactiveAdapterRegistry.getSharedInstance());
    }

    public ServerFilterFieldsHandlerResultHandler(List<HttpMessageWriter<?>> writers,
                                                  RequestedContentTypeResolver resolver, ReactiveAdapterRegistry registry) {
        super(writers, resolver, registry);

        // 这里需要插队，插在<code>ResponseBodyResultHandler</code>之前，适配器在先取到改处理器实例，
        // 否则<code>ResponseBodyResultHandler</code>先于该实例执行则该实例不执行不能实现字段过滤的逻辑
        setOrder(99);
    }

    @Override
    public boolean supports(HandlerResult result) {
        MethodParameter returnType = result.getReturnTypeSource();
        Class<?> containingClass = returnType.getContainingClass();
        return (AnnotatedElementUtils.hasAnnotation(containingClass, ResponseBody.class)
            || returnType.hasMethodAnnotation(ResponseBody.class))
            && returnType.hasMethodAnnotation(JsonBodys.class);
    }

    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        MethodParameter methodParameter = result.getReturnTypeSource();
        JsonBodys annotation = methodParameter.getMethodAnnotation(JsonBodys.class);
        if (annotation == null) {
            return writeBody(result.getReturnValue(), methodParameter, exchange);
        }

        JsonBody[] valueArr = annotation.value();
        if (valueArr == null || valueArr.length == 0) {
            return writeBody(result.getReturnValue(), methodParameter, exchange);
        }

        FilterFieldsJsonSerializer jsonSerializer = new FilterFieldsJsonSerializer(new ObjectMapper());
        for (JsonBody jsonBody : valueArr) {
            jsonSerializer.filter(jsonBody);
        }

        try {
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            // TODO 这里为避免输出的是字符串格式又进行了反序列化，有效率问题，后续处理
            return writeBody(JSON.parse(jsonSerializer.toJson(result.getReturnValue())), methodParameter, exchange);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BizException(ErrType.INTERNAL_SERVER_ERROR.getStatus(), "报文序列化失败", e);
        }
    }

}
