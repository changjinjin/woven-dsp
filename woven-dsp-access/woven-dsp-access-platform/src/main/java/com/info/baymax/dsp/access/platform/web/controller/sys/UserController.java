package com.info.baymax.dsp.access.platform.web.controller.sys;

import com.info.baymax.common.comp.base.MainTableController;
import com.info.baymax.common.comp.serialize.annotation.JsonBody;
import com.info.baymax.common.comp.serialize.annotation.JsonBodys;
import com.info.baymax.common.crypto.CryptoOperation;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.annotation.Cryptoable;
import com.info.baymax.common.crypto.annotation.Decrypt;
import com.info.baymax.common.crypto.annotation.ReturnOperation;
import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.entity.bean.ChangePwd;
import com.info.baymax.dsp.data.sys.entity.security.Role;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.initialize.InitConfig;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.dsp.data.sys.service.security.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "系统管理：系统用户管理", value = "系统用户管理接口定义")
public class UserController implements MainTableController<User> {

    @Autowired
    private InitConfig initConfig;

    @Autowired
    private UserService userService;
    @Autowired
    private TenantService tenantService;

    @Override
    public BaseMaintableService<User> getBaseMaintableService() {
        return userService;
    }

    @JsonBodys({
        @JsonBody(type = User.class, excludes = {"creator", "createTime", "lastModifier", "lastModifiedTime",
            "hdfsSpaceQuota", "moduleVersion", "password", "description", "groupCount", "groupFieldValue"}),
        @JsonBody(type = Role.class, includes = {"id", "name"})})
    @Cryptoable(returnOperation = {
        @ReturnOperation(cryptoOperation = CryptoOperation.Encrypt, cryptoType = CryptoType.AES)})
    @Override
    public Response<IPage<User>> page(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        query = ExampleQuery.builder(query).fieldGroup().andEqualTo("tenantId", SaasContext.getCurrentTenantId()).end();
        IPage<User> page = userService.selectPage(query);
        return Response.ok(page);
    }

    @JsonBodys({
        @JsonBody(type = User.class, excludes = {"creator", "createTime", "lastModifier", "lastModifiedTime",
            "moduleVersion", "password", "description", "groupCount", "groupFieldValue"}),
        @JsonBody(type = Role.class, excludes = {"permissions"})})
    @Cryptoable(returnOperation = {
        @ReturnOperation(cryptoOperation = CryptoOperation.Encrypt, cryptoType = CryptoType.AES)})
    @Override
    public Response<User> infoById(@ApiParam(value = "用户ID", required = true) @RequestParam String id) {
        User t = userService.selectByPrimaryKey(id);
        if (t != null) {
            return Response.ok(t);
        }
        return Response.error(ErrType.ENTITY_NOT_EXIST);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/changePwd")
    @Cryptoable(enableParam = true)
    public Response<?> changePwd(@ApiParam(value = "新密码", required = true) @RequestBody @Decrypt ChangePwd changePwd) {
        if (changePwd == null || StringUtils.isEmpty(changePwd.getNewPass())
            || StringUtils.isEmpty(changePwd.getOldPass())) {
            return Response.error(ErrType.BAD_REQUEST);
        }
        userService.changePwd(changePwd.getOldPass(), changePwd.getNewPass(), initConfig.isPwdStrict());
        return Response.ok();
    }

    @ApiOperation(value = "重置用户密码")
    @PostMapping("/resetPwd")
    public Response<?> resetPwd(@ApiParam(value = "重置密码的用户ID数组", required = true) @RequestParam String[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.error(ErrType.BAD_REQUEST, "请选择需要重置密码的用户！");
        }
        userService.resetPwd(ids, initConfig.getPassword());
        return Response.ok();
    }

    @ApiOperation(value = "修改用户启用状态")
    @PostMapping("/resetStatus")
    public Response<?> resetStatus(
        @ApiParam(value = "修改状态的用户列表，包含用户ID和修改后状态属性值", required = true) @RequestBody List<User> list) {
        if (ICollections.hasNoElements(list)) {
            return Response.error(ErrType.BAD_REQUEST, "请选择启用/停用的用户！");
        }
        userService.resetStatus(list);
        return Response.ok();
    }

    @ApiOperation(value = "获取可用的资源队列")
    @PostMapping("/getResourceQueues")
    public Response<List<String>> getResourceQueues() {
        Tenant currentTenant = tenantService.selectByPrimaryKey(SaasContext.getCurrentTenantId());
        if (currentTenant != null) {
            return Response.ok(currentTenant.getResourceQueues());
        }
        return Response.ok(Arrays.asList("default"));
    }
}
