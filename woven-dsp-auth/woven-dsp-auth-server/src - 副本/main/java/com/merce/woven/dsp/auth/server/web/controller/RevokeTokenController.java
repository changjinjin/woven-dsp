package com.jusfoun.services.auth.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.merce.woven.common.message.result.ErrType;
import com.merce.woven.common.message.result.Response;
import com.merce.woven.dsp.common.constants.AuthConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "认证管理", value = "系统用户认证管理", description = "用户认证相关接口")
@RestController
public class RevokeTokenController {

	@Autowired
	@Qualifier("consumerTokenServices")
	private ConsumerTokenServices consumerTokenServices;

	@ApiOperation(value = "token注销", notes = "Token注销", hidden = false)
	@RequestMapping(value = AuthConstants.TOKEN_REVOKE_ENTRY_POINT, method = { RequestMethod.POST })
	public Response<?> revokeToken(//
			@RequestHeader(value = AuthConstants.TOKEN_HEADER_PARAM, required = true) String token //
	) {
		if (consumerTokenServices.revokeToken(token.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim())) {
			return Response.ok();
		} else {
			return Response.error(ErrType.FAILED, "注销失败");
		}
	}
}
