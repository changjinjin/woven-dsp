package com.info.baymax.common.webmvc.servlet.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.config.serialize.jackson.fieldFilter.FilterFieldsJsonSerializer;
import com.info.baymax.common.core.annotation.JsonBodys;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 说明：json返回结果处理器. <br>
 *
 * @author jingwei.yang
 * @date 2017年11月25日 上午10:26:19
 */
public class ServletFilterFieldsHandlerResultHandler implements HandlerMethodReturnValueHandler, BeanPostProcessor {

    List<ResponseBodyAdvice<Object>> advices = new ArrayList<>();

    private ObjectMapper objectMapper;

    public ServletFilterFieldsHandlerResultHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 判断是否有JsonBody或Json注解
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        // 该Controller类上面存在RestController注解或者该方法上面存在ResponseBody注解，并且该方法上面存在JsonBody注解(过滤所有，暂时不需要判断是否存在JsonBody注解)
        Class<?> declaringClass = returnType.getMethod().getDeclaringClass();
        return (declaringClass.isAnnotationPresent(RestController.class) || returnType.hasMethodAnnotation(ResponseBody.class)) && returnType.hasMethodAnnotation(JsonBodys.class);
    }

    // 有的话就会执行这个方法
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        for (ResponseBodyAdvice<Object> ad : advices) {
            if (ad.supports(returnType, null)) {
                returnValue = ad.beforeBodyWrite(returnValue, returnType, MediaType.APPLICATION_JSON, null,
                    new ServletServerHttpRequest(webRequest.getNativeRequest(HttpServletRequest.class)),
                    new ServletServerHttpResponse(webRequest.getNativeResponse(HttpServletResponse.class)));
            }
        }

        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Annotation[] annos = returnType.getMethodAnnotations();
        // 调用
        FilterFieldsJsonSerializer jsonSerializer = new FilterFieldsJsonSerializer(objectMapper);
        // 如果是JsonBody就循环调用
        Arrays.asList(annos).forEach(a -> {
            if (a instanceof JsonBodys) {
                JsonBodys body = (JsonBodys) a;
                Arrays.asList(body.value()).forEach(json -> {
                    jsonSerializer.filter(json);
                });
            }
        });

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 转换成json返回
        String json = jsonSerializer.toJson(returnValue);
        response.getWriter().write(json);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ResponseBodyAdvice) {
            advices.add((ResponseBodyAdvice<Object>) bean);
        } else if (bean instanceof RequestMappingHandlerAdapter) {
            List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(((RequestMappingHandlerAdapter) bean).getReturnValueHandlers());
            ServletFilterFieldsHandlerResultHandler jsonHandler = null;
            for (int i = 0; i < handlers.size(); i++) {
                HandlerMethodReturnValueHandler handler = handlers.get(i);
                if (handler instanceof ServletFilterFieldsHandlerResultHandler) {
                    jsonHandler = (ServletFilterFieldsHandlerResultHandler) handler;
                    break;
                }
            }
            if (jsonHandler != null) {
                handlers.remove(jsonHandler);
                handlers.add(0, jsonHandler);
                ((RequestMappingHandlerAdapter) bean).setReturnValueHandlers(handlers);
            }
        }
        return bean;
    }

}
