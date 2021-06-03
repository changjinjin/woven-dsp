package com.info.baymax.security.oauth.web.controller;

import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.utils.Base64Utils;
import com.info.baymax.common.utils.crypto.AESUtil;
import com.info.baymax.security.oauth.security.authentication.CustomTokenServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.security.Principal;
import java.util.Map;

@Api(tags = "认证管理", value = "用户认证相关接口", description = "系统用户认证管理")
@RestController
@RequestMapping("/oauth")
@SessionAttributes("authorizationRequest")
public class OAuthController {

    // @Autowired
    // private KeyPair keyPair;
    private static final String KEY = "infoaeskey123456";
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private CustomTokenServices customTokenServices;

    @ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息")
    @GetMapping({"/userinfo"})
    @ResponseStatus(HttpStatus.OK)
    public Principal user(Principal user) {
        return user;
    }

    @ApiOperation(value = "获取token", hidden = true)
    @GetMapping("/token")
    public Response<OAuth2AccessToken> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters)
        throws HttpRequestMethodNotSupportedException {
        if(StringUtil.isNotEmpty(parameters.get("password"))){
            String decodePassword = decodePassword(parameters.get("password"));
            if(StringUtil.isNotEmpty(decodePassword)){
                parameters.put("password", decodePassword);
            }
        }
        return Response.ok(tokenEndpoint.getAccessToken(principal, parameters).getBody());
    }

    @ApiOperation(value = "获取token", hidden = true)
    @PostMapping("/token")
    public Response<OAuth2AccessToken> postAccessToken(Principal principal,
                                                       @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        if(StringUtil.isNotEmpty(parameters.get("password"))){
            String decodePassword = decodePassword(parameters.get("password"));
            if(StringUtil.isNotEmpty(decodePassword)){
                parameters.put("password", decodePassword);
            }
        }
        return Response.ok(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }

    private String decodePassword(String password) {
        if(password.startsWith("AES(")){
            String decryptAES = AESUtil.decrypt(password.substring(4, password.length() - 1), KEY);
            if(Base64Utils.check(decryptAES)){
                String decode = Base64Utils.decode(decryptAES);
                return decode;
            }
        }
        return null;
    }

    @ApiOperation(value = "销毁认证信息（退出登录）")
    @GetMapping("/revoke")
    public Response<?> revoke(
        @ApiParam(value = "token信息", hidden = true) @RequestHeader("Authorization") String accessToken) {
        try {
            customTokenServices.revokeToken(accessToken.substring("Bearer".length()).trim());
        } catch (Exception e) {
        }
        return Response.ok().build();
    }

    // @ApiOperation(value = "验证 JSON Web令牌地址", hidden = true)
    // @ResponseBody
    // @GetMapping("/.well-known/jwks.json")
    // @ApiIgnore
    // public Map<String, Object> getKey() {
    // RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
    // RSAKey key = new RSAKey.Builder(publicKey).build();
    // return new JWKSet(key).toJSONObject();
    // }
}
