package com.info.baymax.common.swagger.config;

import com.info.baymax.common.swagger.binding.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ConfigurationProperties(prefix = Swagger2Properties.PREFIX)
public class Swagger2Properties {
    public static final String PREFIX = "swagger2";

    /**
     * 是否开启文档，可根据发布环境动态配置，如在不同的profiles中设置开启与关闭，生产环境关闭，测试环境和开发环境开放等
     */
    private boolean enabled = true;

    /**
     * 文档扫描包路径
     */
    private String basePackage;

    /**
     * 当前服务文档定义
     */
    private ApiInfo apiInfo;

    /**
     * 全局参数
     */
    private List<Parameter> globalParameters;

    /**
     * SwaggerResource 定义列表
     */
    private List<SwaggerResource> resources;

    public List<springfox.documentation.service.Parameter> parseGlobalParameters() {
        List<Parameter> globalParameters = getGlobalParameters();
        if (globalParameters != null && !globalParameters.isEmpty()) {
            return globalParameters.stream().map(t -> t.get()).collect(Collectors.toList());
        }
        return null;
    }

}
