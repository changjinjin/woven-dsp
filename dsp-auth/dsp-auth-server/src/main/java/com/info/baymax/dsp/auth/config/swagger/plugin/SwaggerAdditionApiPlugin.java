package com.info.baymax.dsp.auth.config.swagger.plugin;

import com.info.baymax.dsp.auth.config.swagger.api.OauthAuthorizeApiDescription;
import com.info.baymax.dsp.auth.config.swagger.api.OauthCheckTokenApiDescription;
import com.info.baymax.dsp.auth.config.swagger.api.OauthTokenApiDescription;
import com.info.baymax.dsp.auth.config.swagger.api.OauthTokenKeyApiDescription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * /oauth/token 接口文档
 *
 * @author yjw
 * @date 2019年1月27日 上午9:57:20
 */
@Component
public class SwaggerAdditionApiPlugin implements ApiListingScannerPlugin {

    @Value("${server.reactive.context-path:${server.servlet.context-path:}}")
    private String contextPath;

    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        return new ArrayList<ApiDescription>(//
            Arrays.asList(//
                new OauthTokenApiDescription(contextPath).get(), //
                new OauthAuthorizeApiDescription(contextPath).get(), //
                new OauthCheckTokenApiDescription(contextPath).get(), //
                new OauthTokenKeyApiDescription(contextPath).get() //
            ));
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}