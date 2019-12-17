package com.info.baymax.dsp.auth.web.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "认证管理", value = "用户认证相关接口", description = "系统用户认证管理")
@RestController
//@RequestMapping("/oauth")
@SessionAttributes("authorizationRequest")
public class OAuthController {

    @ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息", hidden = false)
    @GetMapping({"/user"})
    @ResponseStatus(HttpStatus.OK)
    public Principal user(Principal user) {
        return user;
    }
}
