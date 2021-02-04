package com.info.baymax.dsp.gateway.rest;

import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.service.security.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: haijun
 * @Date: 2020/10/13 14:09
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "获取当前用户", value = "系统用户管理接口定义")
public class UserResource {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取可用的资源队列")
    @PostMapping("/current")
    public Response<User> getCurrentUser() {
        String userId = SaasContext.getCurrentUserId();
        User t = userService.selectByPrimaryKey(userId);
        if (t != null) {
            return Response.ok(t);
        }
        return Response.error(ErrType.ENTITY_NOT_EXIST).build();
    }
}
