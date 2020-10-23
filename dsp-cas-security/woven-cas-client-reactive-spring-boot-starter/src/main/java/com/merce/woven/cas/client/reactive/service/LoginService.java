package com.merce.woven.cas.client.reactive.service;

import com.merce.woven.cas.client.reactive.vo.LoginParamDto;
import com.merce.woven.cas.client.reactive.vo.LoginVo;
import org.springframework.web.server.ServerWebExchange;

public interface LoginService {
    LoginVo restLogin(LoginParamDto dto, ServerWebExchange exchange);
    String loginOut(ServerWebExchange exchange);
}
