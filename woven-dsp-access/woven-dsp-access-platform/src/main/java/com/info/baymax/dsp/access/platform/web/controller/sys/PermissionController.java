package com.info.baymax.dsp.access.platform.web.controller.sys;

import com.info.baymax.common.comp.serialize.annotation.JsonBody;
import com.info.baymax.common.comp.serialize.annotation.JsonBodys;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perm")
@Api(tags = "认证与授权：系统权限管理", value = "系统权限管理接口定义")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "保存权限信息")
    @PostMapping("/save")
    public Response<?> save(@ApiParam(value = "权限信息", required = true) @RequestBody Permission t) {
        permissionService.save(t);
        return Response.ok();
    }

    @ApiOperation(value = "编辑权限信息")
    @PostMapping("/update")
    public Response<?> update(@ApiParam(value = "权限信息", required = true) @RequestBody Permission t) {
        permissionService.update(t);
        return Response.ok();
    }

    @ApiOperation(value = "根据ID删除权限信息")
    @PostMapping("/deleteById/{id}")
    public Response<?> deleteById(@ApiParam(value = "权限ID", required = true) @PathVariable("id") Long id) {
        permissionService.deleteOnCascadeById(id);
        return Response.ok();
    }

    @ApiOperation(value = "根据ID查询权限信息")
    @PostMapping("/infoById")
    @JsonBodys({@JsonBody(type = Permission.class, includes = {"id", "code", "name", "type", "url", "description",
        "enabled", "order", "route", "icon"})})
    public Response<Permission> infoById(@ApiParam(value = "权限ID", required = true) @RequestBody String id) {
        return Response.ok(permissionService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "查询权限列表，树状数据结构")
    @PostMapping("/rootsTree")
    @JsonBodys({@JsonBody(type = Permission.class, includes = {"id", "code", "name", "type", "url", "description",
        "enabled", "children", "order", "route", "icon"})})
    public Response<List<Permission>> rootsTree() {
        List<Permission> roots = permissionService.findRootsTree();
        if (ICollections.hasNoElements(roots)) {
            return Response.error(ErrType.ENTITY_QUERY_LIST_ERROR, "无可分配的权限列表");
        }
        return Response.ok(roots);
    }
}
