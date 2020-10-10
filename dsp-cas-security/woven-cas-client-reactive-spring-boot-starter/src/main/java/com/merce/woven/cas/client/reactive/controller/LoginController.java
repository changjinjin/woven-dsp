package com.merce.woven.cas.client.reactive.controller;

import com.merce.woven.cas.client.reactive.config.CasServiceProperties;
import com.merce.woven.cas.client.reactive.service.LoginService;
import com.merce.woven.cas.client.reactive.util.CASAuthUtil;
import com.merce.woven.cas.client.reactive.vo.LoginParamDto;
import com.merce.woven.cas.client.reactive.vo.LoginVo;

import cn.hutool.json.JSONObject;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

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

//    @PostMapping("/cas/user")
//    public Mono<ResponseEntity<JSONObject>> getLogin(ServerWebExchange exchange) {
//        return ReactiveSecurityContextHolder.getContext().map(context -> {
//            Authentication authentication = context.getAuthentication();
//            if (authentication != null) {
//                User principal = (User)authentication.getPrincipal();
//                String username = principal.getUsername();
//                List<GrantedAuthority> authorities = (List<GrantedAuthority>)authentication.getAuthorities();
//                List<String> auths = new LinkedList<>();
//                boolean admin = false;
//                for(GrantedAuthority auth : authorities){
//                    auths.add(auth.getAuthority());
//                    if(auth.getAuthority().equalsIgnoreCase("ROLE_ADMIN") || auth.getAuthority().equalsIgnoreCase("SYSTEM")){
//                        admin = true;
//                    }
//                }
//
//                JSONObject map = new JSONObject();
//                map.put("username", username);
//                map.put("admin", admin);
//                map.put("authorities", auths);
//
//                return ResponseEntity.ok(map);
//            }
//            return null;
//        }).defaultIfEmpty(ResponseEntity.ok(null));
//    }

    @PostMapping("/cas/platform")
    public Mono<ResponseEntity<JSONObject>> toPlatform(ServerWebExchange exchange) {
        JSONObject map = new JSONObject();
        map.put("status", 200);
        map.put("mode", casProperties.getMode());
        map.put("redirectUrl", casProperties.getPlatformServer());
        return Mono.justOrEmpty(ResponseEntity.ok(map));
    }
}
