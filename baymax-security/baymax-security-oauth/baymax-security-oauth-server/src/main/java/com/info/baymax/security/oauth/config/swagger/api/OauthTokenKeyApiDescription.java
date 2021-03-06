package com.info.baymax.security.oauth.config.swagger.api;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import com.info.baymax.dsp.data.sys.constant.AuthConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Parameter;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * /oauth/token_key 接口文档
 *
 * @author jingwei.yang
 * @date 2019年12月11日 上午11:55:00
 */
public class OauthTokenKeyApiDescription extends AbstractSwaggerAdditionApiDescription {

    public OauthTokenKeyApiDescription(String contextPath) {
        super(contextPath, AuthConstants.TOKEN_KEY_ENTRY_POINT);
    }

    @Override
    public ApiDescription get() {
        return new ApiDescription("Auth", getFullPath(), // url
            "提供公有密匙的端点", // 描述
            Arrays.asList(new OperationBuilder(new CachingOperationNameGenerator())//
                .method(HttpMethod.POST)// http请求类型
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))//
                .summary("提供公有密匙的端点，如果你使用JWT令牌的话")//
                .notes("提供公有密匙的端点，如果你使用JWT令牌的话")// 方法描述
                .tags(Sets.newHashSet("认证管理"))// 归类标签
                .parameters(parameters())//
                .build()),
            false);
    }

    private List<Parameter> parameters() {
        return Arrays.asList(new ParameterBuilder()//
            .description("令牌")//
            .type(new TypeResolver().resolve(String.class))//
            .name("token")//
            .parameterType("header")//
            .parameterAccess("access")//
            .required(true)//
            .modelRef(new ModelRef("string")) //
            .build());
    }
}
