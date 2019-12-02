package com.jusfoun.services.auth.server.web.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jusfoun.services.auth.api.remote.AuthRemoteService;
import com.merce.woven.dsp.common.constants.AuthConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 说明： 授权管理. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年9月26日 下午1:52:50
 */
@Api(tags = "认证管理", value = "用户认证相关接口", description = "系统用户认证管理")
@RestController
@SessionAttributes("authorizationRequest")
public class OAuthController implements AuthRemoteService {

	/**
	 * 说明：获取用户信息. <br>
	 *
	 * @author yjw@jusfoun.com
	 * @date 2017年9月26日 下午1:52:04
	 * @param user 用户信息
	 * @return 用户信息
	 */
	@ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息", hidden = false)
	// @Logable(value = "获取登录用户信息", path = "获取登录用户信息")
	@GetMapping({ "/user" })
	@ResponseStatus(HttpStatus.OK)
	public Principal user(//
			@ApiParam(value = "令牌", required = true) @RequestHeader(name = AuthConstants.TOKEN_HEADER_PARAM, required = true) String token, //
			Principal user//
	) {
		return user;
	}

}
