package com.info.baymax.dsp.auth.web.controller;

import com.info.baymax.common.message.result.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Api(tags = "认证管理", value = "用户认证相关接口", description = "系统用户认证管理")
@RestController
@RequestMapping("/oauth")
@SessionAttributes("authorizationRequest")
public class OAuthController {

	@ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息", hidden = false)
	@GetMapping({ "/user" })
	@ResponseStatus(HttpStatus.OK)
	public Principal user(Principal user) {
		return user;
	}

	@Autowired
	private TokenEndpoint tokenEndpoint;

	@ApiOperation(value = "获取token", hidden = true)
	@GetMapping("/token")
	public Response<OAuth2AccessToken> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters)
			throws HttpRequestMethodNotSupportedException {
		return Response.ok(tokenEndpoint.getAccessToken(principal, parameters).getBody());
	}

	@ApiOperation(value = "获取token", hidden = true)
	@PostMapping("/token")
	public Response<OAuth2AccessToken> postAccessToken(Principal principal,
			@RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
		return Response.ok(tokenEndpoint.postAccessToken(principal, parameters).getBody());
	}
}
