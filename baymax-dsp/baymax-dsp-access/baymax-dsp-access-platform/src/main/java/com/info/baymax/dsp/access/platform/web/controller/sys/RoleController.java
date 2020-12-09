package com.info.baymax.dsp.access.platform.web.controller.sys;

import com.info.baymax.common.annotation.JsonBody;
import com.info.baymax.common.annotation.JsonBodys;
import com.info.baymax.common.comp.base.MainTableController;
import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.result.Response;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import com.info.baymax.dsp.data.sys.entity.security.Role;
import com.info.baymax.dsp.data.sys.service.security.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/role")
@Api(tags = "系统管理：系统角色管理", value = "系统角色管理接口定义")
public class RoleController implements MainTableController<Role> {

    @Autowired
    private RoleService roleService;

    @Override
    public BaseMaintableService<Role> getBaseMaintableService() {
        return roleService;
    }

    @ApiOperation(value = "编辑角色资源目录权限")
    @PostMapping("/updateRrrs")
    public Response<?> updateRrrs(@ApiParam(value = "角色信息", required = true) @RequestBody Role t) {
        roleService.updateRrrs(t);
        return Response.ok().build();
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
    @Override
    public Response<IPage<Role>> page(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        query = ExampleQuery.builder(query)
            .fieldGroup(FieldGroup.builder().andEqualTo("tenantId", SaasContext.getCurrentTenantId()));
        IPage<Role> page = roleService.selectPage(query);
        return Response.ok(page);
    }

    @ApiOperation(value = "根据ID查询角色信息")
    @GetMapping("/infoById")
    @JsonBodys({@JsonBody(type = Role.class, includes = {"id", "name", "permissions", "permIds", "rrrfGroups"}),
        @JsonBody(type = Permission.class, includes = {"id", "name", "type", "url", "halfSelect"})})
    public Response<Role> infoById(@ApiParam(value = "角色ID", required = true) @RequestParam String id) {
        return Response.ok(roleService.selectWithPermissionsAndResourcesRefsById(id));
    }

    @ApiOperation(value = "修改角色启用状态")
    @PostMapping("/resetStatus")
    public Response<?> resetStatus(
        @ApiParam(value = "修改状态的角色列表，包含角色ID和修改后状态属性值", required = true) @RequestBody @NotEmpty List<Role> list) {
        roleService.resetStatus(list);
        return Response.ok().build();
    }
}
