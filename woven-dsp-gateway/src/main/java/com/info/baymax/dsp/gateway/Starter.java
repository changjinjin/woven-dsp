package com.info.baymax.dsp.gateway;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.auth.api.utils.SecurityUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "test", description = "test")
@RestController
@SpringCloudApplication
@EnableZuulProxy
@EnableOAuth2Sso
@ComponentScan(basePackages = { "com.info.baymax" })
public class Starter {
	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}

	@ApiOperation("test")
	@GetMapping("test")
	public Response<Object> test() {
		Authentication currentAuthentication = SecurityUtils.getCurrentAuthentication();
		if (currentAuthentication instanceof OAuth2Authentication) {
			OAuth2Authentication auth = (OAuth2Authentication) currentAuthentication;
			Authentication userAuthentication = auth.getUserAuthentication();

			Map<String, Object> details = (Map<String, Object>) userAuthentication.getDetails();
			Map<String, Object> userAuthenticationMap = (Map<String, Object>) details.get("userAuthentication");
			if (userAuthenticationMap == null) {
				return null;
			}
			return Response.ok(userAuthenticationMap);
		}
		return Response.ok();
	}
}
