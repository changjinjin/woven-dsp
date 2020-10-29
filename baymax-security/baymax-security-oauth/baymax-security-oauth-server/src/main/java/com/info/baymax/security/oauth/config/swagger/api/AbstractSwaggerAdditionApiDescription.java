package com.info.baymax.security.oauth.config.swagger.api;

import lombok.Getter;

public abstract class AbstractSwaggerAdditionApiDescription implements SwaggerAdditionApiDescription {

    @Getter
    private String fullPath;

    public AbstractSwaggerAdditionApiDescription(String contextPath, String relativePath) {
        if (contextPath != null && contextPath.length() > 0) {
            this.fullPath = trimSlash(contextPath + relativePath);
        }
    }

    public String trimSlash(String src) {
        return src.replaceAll("//", "/");
    }
}
