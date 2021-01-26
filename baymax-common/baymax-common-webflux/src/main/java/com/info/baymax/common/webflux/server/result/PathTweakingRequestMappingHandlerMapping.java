package com.info.baymax.common.webflux.server.result;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class PathTweakingRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo methodMapping = super.getMappingForMethod(method, handlerType);
        if (methodMapping == null)
            return null;
        if (method.getDeclaringClass() == handlerType) {
            return methodMapping;
        }
        RequestMapping declaredAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
        Method[] declaredMethods = handlerType.getDeclaredMethods();
        RequestMapping requestMapping;
        for (Method declaredMethod : declaredMethods) {
            requestMapping = declaredMethod.getDeclaredAnnotation(RequestMapping.class);
            if (requestMapping != null && declaredAnnotation.value().equals(requestMapping.value())
                && declaredAnnotation.method()[0].equals(requestMapping.method()[0])) {
                return null;
            }
        }
        return methodMapping;
    }
}