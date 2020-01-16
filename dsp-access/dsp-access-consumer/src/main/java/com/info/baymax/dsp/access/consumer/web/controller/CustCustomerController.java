package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.crypto.annotation.Cryptoable;
import com.info.baymax.common.crypto.annotation.Decrypt;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.sys.entity.bean.ChangePwd;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统管理：消费者管理", description = "消费者管理")
@RestController
@RequestMapping("/cust")
public class CustCustomerController {

    @Autowired
    private CustomerService consumerService;

    @ApiOperation(value = "修改密码")
    @PostMapping("/changePwd")
    @Cryptoable(enableParam = true)
    public Response<?> changePwd(@ApiParam(value = "新密码", required = true) @RequestBody @Decrypt ChangePwd changePwd) {
        if (changePwd == null || StringUtils.isEmpty(changePwd.getNewPass())
            || StringUtils.isEmpty(changePwd.getOldPass())) {
            return Response.error(ErrType.BAD_REQUEST);
        }
        consumerService.changePwd(changePwd.getOldPass(), changePwd.getNewPass(), false);
        return Response.ok();
    }

}
