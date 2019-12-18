package com.info.baymax.dsp.access.platform.web.controller.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.common.comp.serialize.annotation.JsonBody;
import com.info.baymax.common.comp.serialize.annotation.JsonBodys;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import com.info.baymax.dsp.data.sys.entity.security.Role;
import com.info.baymax.dsp.data.sys.service.security.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/role")
@Api(tags = "认证与授权：系统角色管理", value = "系统角色管理接口定义")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("保存角色信息")
    @PostMapping("/save")
    public Response<?> save(@ApiParam(value = "角色信息，可包含权限列表属性", required = true) @RequestBody Role t) {
        if (t == null) {
            return Response.error(ErrType.BAD_REQUEST, "请填写角色信息");
        }
        roleService.save(t);
        return Response.ok();
    }

    @ApiOperation(value = "编辑角色信息")
    @PostMapping("/update")
    public Response<?> update(@ApiParam(value = "角色信息", required = true) @RequestBody Role t) {
        roleService.update(t);
        return Response.ok();
    }

    @ApiOperation(value = "编辑角色资源目录权限")
    @PostMapping("/updateRrrs")
    public Response<?> updateRrrs(@ApiParam(value = "角色信息", required = true) @RequestBody Role t) {
        roleService.updateRrrs(t);
        return Response.ok();
    }

    @ApiOperation(value = "查询角色列表")
    @PostMapping("/list")
    @JsonBodys({@JsonBody(type = Role.class, includes = {"id", "name", "permissions", "enabled"}),
        @JsonBody(type = Permission.class, includes = {"id", "name"})})
    public Response<List<Role>> list() {
        return Response.ok(roleService.findAllByTenantId(SaasContext.getCurrentTenantId()));
    }

    @ApiOperation(value = "分页查询角色信息")
    @PostMapping("/listPage")
    @JsonBodys({@JsonBody(type = Role.class, includes = {"id", "name", "permissions", "enabled"}),
        @JsonBody(type = Permission.class, includes = {"id", "name"})})
    public Response<IPage<Role>> listPage(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        query = ExampleQuery.builder(query).fieldGroup().andEqualTo("tenantId", SaasContext.getCurrentTenantId()).end();
        IPage<Role> page = roleService.selectPage(query);
        return Response.ok(page);
    }

    @ApiOperation(value = "批量删除角色")
    @PostMapping("/deleteByIds")
    public Response<?> deleteByIds(@ApiParam(value = "删除的角色ID数组", required = true) @RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Response.error(ErrType.BAD_REQUEST, "请选择需要删除的角色！");
        }
        roleService.deleteByIds(ids);
        return Response.ok();
    }

    @ApiOperation(value = "根据ID查询角色信息")
    @GetMapping("/infoById")
    @JsonBodys({@JsonBody(type = Role.class, includes = {"id", "name", "permissions", "permIds", "rrrfGroups"}),
        @JsonBody(type = Permission.class, includes = {"id", "name", "type", "url", "halfSelect"})})
    public Response<Role> infoById(@ApiParam(value = "角色ID", required = true) @RequestParam Long id) {
        return Response.ok(roleService.selectWithPermissionsAndResourcesRefsById(id));
    }

    @ApiOperation(value = "修改角色启用状态")
    @PostMapping("/resetStatus")
    public Response<?> resetStatus(
        @ApiParam(value = "修改状态的角色列表，包含角色ID和修改后状态属性值", required = true) @RequestBody List<Role> list) {
        if (ICollections.hasNoElements(list)) {
            return Response.error(ErrType.BAD_REQUEST, "请选择启用/停用的角色！");
        }
        roleService.resetStatus(list);
        return Response.ok();
    }
}
