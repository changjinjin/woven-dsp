package com.info.baymax.dsp.access.platform.web.controller.sys;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.common.crypto.CryptoOperation;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.annotation.Cryptoable;
import com.info.baymax.common.crypto.annotation.Decrypt;
import com.info.baymax.common.crypto.annotation.ReturnOperation;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.TenantRegisterBean;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.initialize.InitConfig;
import com.info.baymax.dsp.data.sys.initialize.TenantInitializer;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.dsp.data.sys.service.security.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/tenant")
@Api(tags = "认证与授权：租户管理", value = "租户管理接口定义")
public class TenantController {

    @Autowired
    private TenantService tenantService;
    @Autowired
    private UserService userService;
    @Autowired
    private InitConfig initConfig;

    @PostMapping("/save")
    @ApiOperation(value = "创建租户")
    @Cryptoable(enableParam = true)
    public Response<Long> create(
        @ApiParam(value = "租户注册信息", required = true) @RequestBody @Decrypt TenantRegisterBean tenant) {
        tenantService.createTenant(initConfig, tenant);
        return Response.ok();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改更新租户")
    @Cryptoable(enableParam = true)
    public Response<?> update(@ApiParam(value = "租户ID", required = true) @PathVariable("id") String id,
                              @ApiParam(value = "租户注册信息", required = true) @RequestBody @Decrypt TenantRegisterBean tenant) {
        tenantService.updateTenant(initConfig, tenant);
        return Response.ok();
    }

    @GetMapping("/infoById")
    @ApiOperation(value = "根据租户id查询租户")
    @Cryptoable(returnOperation = {
        @ReturnOperation(cryptoOperation = CryptoOperation.Encrypt, cryptoType = CryptoType.AES)})
    public Response<TenantRegisterBean> infoById(@ApiParam(value = "租户ID") @PathVariable("id") String id)
        throws Exception {
        Tenant tenant = tenantService.selectByPrimaryKey(id);
        if (tenant == null) {
            throw new ControllerException(ErrType.ENTITY_NOT_EXIST, "租户不存在");
        }
        User admin = userService.findByTenantAndUsername(tenant.getId(), TenantInitializer.INIT_ADMIN_LOGINID);
        TenantRegisterBean tenantRegisterBean = new TenantRegisterBean();
        BeanUtils.copyProperties(tenant, tenantRegisterBean);
        tenantRegisterBean.setAdminPassword(admin.getPassword());
        tenantRegisterBean.setAdminSpaceQuota(admin.getHdfsSpaceQuota());
        return Response.ok(tenantRegisterBean);
    }

    @PostMapping("page")
    @ApiOperation("条件查询租户(分页/排序)")
    public Response<IPage<Tenant>> page(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        query = ExampleQuery.builder(query).fieldGroup().andNotEqualTo("name", "root").end();
        return Response.ok(tenantService.selectPage(query));
    }

    @PostMapping("enable")
    @ApiOperation(value = "启用租户")
    public Response<?> enable(@ApiParam(value = "ID集合", required = true) @RequestBody String[] ids) {
        tenantService.enableTenants(ids);
        return Response.ok();
    }

    @PostMapping("disable")
    @ApiOperation(value = "停用租户")
    public Response<?> disable(@ApiParam(value = "ID集合", required = true) @RequestBody String[] ids) {
        tenantService.disableTenants(ids);
        return Response.ok();
    }

}
