package com.info.baymax.security.cas.reactive.client.controller;

import cn.hutool.json.JSONObject;
import com.info.baymax.security.cas.reactive.client.config.CasServiceProperties;
import com.info.baymax.security.cas.reactive.client.service.LoginService;
import com.info.baymax.security.cas.reactive.client.vo.LoginParamDto;
import com.info.baymax.security.cas.reactive.client.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private CasServiceProperties casProperties;

	@GetMapping("/cas/login_check")
	public Mono<ResponseEntity<Boolean>> loginCheck(ServerWebExchange exchange) {
		return ReactiveSecurityContextHolder.getContext().map(context -> {
			Authentication authentication = context.getAuthentication();
			if (authentication == null) {
				return ResponseEntity.ok(false);
			}
			return ResponseEntity.ok(true);
		}).defaultIfEmpty(ResponseEntity.ok(false));
	}

	@PostMapping("/cas/tickets")
	public Mono<ResponseEntity<LoginVo>> restLogin(@RequestBody LoginParamDto dto, ServerWebExchange exchange) {
		return Mono.justOrEmpty(ResponseEntity.ok(loginService.restLogin(dto, exchange)));
	}

	@PostMapping("/cas/platform")
	public Mono<ResponseEntity<JSONObject>> toPlatform(ServerWebExchange exchange) {
		JSONObject map = new JSONObject();
		map.put("status", 200);
		map.put("mode", casProperties.getMode());
		map.put("redirectUrl", casProperties.getPlatformServer());
		return Mono.justOrEmpty(ResponseEntity.ok(map));
	}
}
