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
 * /oauth/authorize 接口文档
 *
 * @author yjw
 * @date 2019年1月27日 上午10:43:20
 */
public class OauthAuthorizeApiDescription extends AbstractSwaggerAdditionApiDescription {

    public OauthAuthorizeApiDescription(String contextPath) {
        super(contextPath, AuthConstants.TOKEN_AUTHORIZE_ENTRY_POINT);
    }

    @Override
    public ApiDescription get() {
        return new ApiDescription("Auth", getFullPath(), // url
            "资源授权端点", // 描述
            Arrays.asList(new OperationBuilder(new CachingOperationNameGenerator())//
                .method(HttpMethod.GET)// http请求类型
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))//
                .summary("资源授权端点，CODE方式认证请求")//
                .notes("资源授权端点，CODE方式认证请求")// 方法描述
                .tags(Sets.newHashSet("认证管理"))// 归类标签
                .parameters(parameters())//
                .build()),
            false);
    }

    private List<Parameter> parameters() {
        return Arrays.asList(//
            new ParameterBuilder()//
                .description("表示客户端的ID，代表哪个应用请求验证")//
                .type(new TypeResolver().resolve(String.class))//
                .name("client_id")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(true)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("表示授权类型，这里用的是code方式")//
                .type(new TypeResolver().resolve(String.class))//
                .name("response_type")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(true)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("表示重定向URI，验证以后的回调地址，一般用来接收返回的code，以及做下一步处理")//
                .type(new TypeResolver().resolve(String.class))//
                .name("redirect_uri")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(true)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("表示申请的权限范围")//
                .type(new TypeResolver().resolve(String.class))//
                .name("scope")//
                .parameterType("query")//
                .parameterAccess("access")//
                .defaultValue("read/write")//
                .required(true)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。作为安全校验")//
                .type(new TypeResolver().resolve(String.class))//
                .name("state")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(true)//
                .modelRef(new ModelRef("string")) //
                .build() //
        );
    }
}