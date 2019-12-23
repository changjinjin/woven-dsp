package com.info.baymax.dsp.access.platform.web.controller.sys;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.comp.serialize.annotation.JsonBody;
import com.info.baymax.common.comp.serialize.annotation.JsonBodys;
import com.info.baymax.common.crypto.annotation.Cryptoable;
import com.info.baymax.common.crypto.annotation.Decrypt;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.platform.config.SysInitConfig;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "系统管理：消费者管理", description = "消费者管理")
@RestController
@RequestMapping("/cust")
public class CustomerController implements BaseEntityController<Customer> {

    @Autowired
    private CustomerService consumerService;
    @Autowired
    private SysInitConfig sysInitConfig;

    @Override
    public BaseEntityService<Customer> getBaseEntityService() {
        return consumerService;
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("page")
    @ResponseBody
    @JsonBodys({@JsonBody(type = Customer.class, excludes = "password")})
    @Override
    public Response<IPage<Customer>> page(@ApiParam(value = "查询条件") @RequestBody ExampleQuery query) {
        return BaseEntityController.super.page(query);
    }

    @ApiOperation(value = "查询详情")
    @GetMapping("infoById")
    @ResponseBody
    @JsonBodys({@JsonBody(type = Customer.class, excludes = "password")})
    @Override
    public Response<Customer> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam Long id) {
        return BaseEntityController.super.infoById(id);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/changePwd")
    @Cryptoable(enableParam = true)
    public Response<?> changePwd(@ApiParam(value = "新密码", required = true) @RequestBody @Decrypt ChangePwd changePwd) {
        if (changePwd == null || StringUtils.isEmpty(changePwd.getNewPass())
            || StringUtils.isEmpty(changePwd.getOldPass())) {
            return Response.error(ErrType.BAD_REQUEST);
        }
        consumerService.changePwd(changePwd.getOldPass(), changePwd.getNewPass(), sysInitConfig.isPwdStrict());
        return Response.ok();
    }

    @ApiOperation(value = "重置用户密码")
    @PostMapping("/resetPwd")
    public Response<?> resetPwd(@ApiParam(value = "重置密码的用户ID数组", required = true) @RequestParam Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.error(ErrType.BAD_REQUEST, "请选择需要重置密码的用户！");
        }
        consumerService.resetPwd(ids, sysInitConfig.getPassword());
        return Response.ok();
    }

    @ApiOperation(value = "修改用户启用停用状态", notes = "根据ID修改用户的启用或停用状态，需要传用户ID和修改的目标状态值（0-停用，1-启用），批量操作")
    @PostMapping("/resetStatus")
    public Response<?> resetStatus(@ApiParam(value = "修改状态的对象列表", required = true) @RequestBody List<Customer> list) {
        if (ICollections.hasNoElements(list)) {
            return Response.error(ErrType.BAD_REQUEST, "请选择需要修改状态的用户！");
        }
        consumerService.resetStatus(list);
        return Response.ok();
    }

}
