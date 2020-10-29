package com.info.baymax.security.cas.reactive.client.service;

import com.info.baymax.security.cas.reactive.client.vo.LoginParamDto;
import com.info.baymax.security.cas.reactive.client.vo.LoginVo;
import org.springframework.web.server.ServerWebExchange;

public interface LoginService {
    LoginVo restLogin(LoginParamDto dto, ServerWebExchange exchange);

    String loginOut(ServerWebExchange exchange);
}
