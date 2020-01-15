package com.info.baymax.dsp.gateway.config;

import java.util.List;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

public class CustomSwaggerResourcesProvider implements SwaggerResourcesProvider {

    private List<SwaggerResource> resources;

    public CustomSwaggerResourcesProvider(List<SwaggerResource> resources) {
        this.resources = resources;
    }

    @Override
    public List<SwaggerResource> get() {
        return resources;
    }
}
