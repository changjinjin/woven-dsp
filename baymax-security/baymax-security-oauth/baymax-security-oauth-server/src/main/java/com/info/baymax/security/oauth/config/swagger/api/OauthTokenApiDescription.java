package com.info.baymax.security.oauth.config.swagger.api;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import com.info.baymax.dsp.data.sys.constant.AuthConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Parameter;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * /oauth/token 接口文档
 *
 * @author yjw
 * @date 2019年1月27日 上午9:57:20
 */
public class OauthTokenApiDescription extends AbstractSwaggerAdditionApiDescription {

    public OauthTokenApiDescription(String contextPath) {
        super(contextPath, AuthConstants.TOKEN_ENTRY_POINT);
    }

    @Override
    public ApiDescription get() {
        return new ApiDescription("Auth", getFullPath(), // url
            "token获取端点", // 描述
            Arrays.asList(new OperationBuilder(new CachingOperationNameGenerator())//
                .method(HttpMethod.POST)// http请求类型
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))//
                .consumes(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))//
                .summary("获取token")//
                .notes("获取token")// 方法描述
                .tags(Sets.newHashSet("认证管理"))// 归类标签
                .parameters(parameters())//
                .responseModel(new ModelRef("OAuth2AccessToken"))//
                .build()),
            false);
    }

    private List<Parameter> parameters() {
        return Arrays.asList(new ParameterBuilder()//
                .description(
                    "Basic认证客户端授权令牌：client_id和client_secret使用Base64编码（'Basic '+base64({client_id}:{client_secret})）")//
                .type(new TypeResolver().resolve(String.class))//
                .name(AuthConstants.TOKEN_HEADER_PARAM)//
                .modelRef(new ModelRef("string"))//
                .parameterType("header")//
                .required(false)//
                .allowableValues(new AllowableListValues(Arrays.asList("Basic ZHNwOjEyMzQ1Ng==",
                    "Basic Y3VzdG9tZXI6MTIzNDU2", "Basic YmF5bWF4OjEyMzQ1Ng=="), "string"))//
                .defaultValue("Basic ZHNwOjEyMzQ1Ng==")//
                .build(),
            new ParameterBuilder()//
                .description("鉴权方式:\n"//
                    + "1、authorization_code — 授权码模式(即先登录获取code,再获取token)。\n"//
                    + "2、password — 密码模式(将用户名,密码传过去,直接获取token)。\n"//
                    + "3、client_credentials — 客户端模式(无用户,用户向客户端注册,然后客户端以自己的名义向’服务端’获取资源)。\n"//
                    + "4、implicit — 简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)。\n"//
                    + "5、refresh_token — 刷新access_token。 "//
                    + "6、tenant_password — 租户密码认证方式，相对于password模式多了租户信息。 ")//
                .type(new TypeResolver().resolve(String.class))//
                .name("grant_type")//
                .allowableValues(new AllowableListValues(Arrays.asList("manager_password", "customer_password",
                    "password", "refresh_token", "authorization_code", "client_credentials", "implicit "),
                    "string"))//
                .defaultValue("manager_password")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(),
            new ParameterBuilder()//
                .description("用户名（grant_type为password时使用）")//
                .type(new TypeResolver().resolve(String.class))//
                .name("username")//
                .defaultValue("admin")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(),
            new ParameterBuilder()//
                .description("密码（grant_type为password时使用）")//
                .type(new TypeResolver().resolve(String.class))//
                .name("password")//
                .defaultValue("123456")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("授权码（grant_type为authorization_code时使用）")//
                .type(new TypeResolver().resolve(String.class))//
                .name("code")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("重定向地址（grant_type为authorization_code时使用）")//
                .type(new TypeResolver().resolve(String.class))//
                .name("redirect_uri")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("申请的权限范围：read write trust")//
                .type(new TypeResolver().resolve(String.class))//
                .name("scope")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。作为安全校验")//
                .type(new TypeResolver().resolve(String.class))//
                .name("state")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("刷新token，grant_type为refresh_token时使用，用于当acess_token快过期时更新access_token避免过期")//
                .type(new TypeResolver().resolve(String.class))//
                .name("refresh_token")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("图片验证码标示")//
                .type(new TypeResolver().resolve(String.class))//
                .name("uuid")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("租户")//
                .type(new TypeResolver().resolve(String.class))//
                .name("tenant")//
                .defaultValue("default")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build(), //
            new ParameterBuilder()//
                .description("版本")//
                .type(new TypeResolver().resolve(String.class))//
                .name("version")//
                .defaultValue("Baymax-3.0.0.23-20180606")//
                .parameterType("query")//
                .parameterAccess("access")//
                .required(false)//
                .modelRef(new ModelRef("string")) //
                .build() //
        );
    }

}