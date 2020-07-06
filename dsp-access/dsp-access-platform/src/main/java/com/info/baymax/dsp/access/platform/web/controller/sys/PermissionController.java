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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/perm")
@Api(tags = "系统管理：系统权限管理", value = "系统权限管理接口定义")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "添加记录", notes = "新建数据记录，新建时主键为空值")
    @PostMapping("/save")
    public Response<?> save(@ApiParam(value = "待新建记录", required = true) @RequestBody @Valid Permission t) {
        permissionService.saveOrUpdate(t);
        return Response.ok(t.getId());
    }

    @ApiOperation(value = "修改记录", notes = "编辑数据记录，编辑时根据主键查找修改记录，ID值不能为空，当只需要更新部分字段时可只传部分字段的值，其他字段值为空，或者传全部字段")
    @PostMapping("/update")
    public Response<?> update(@ApiParam(value = "待编辑记录", required = true) @RequestBody @Valid Permission t) {
        permissionService.saveOrUpdate(t);
        return Response.ok().build();
    }

    @ApiOperation(value = "单个删除", notes = "根据ID每次删除一条数据，ID不能为空")
    @GetMapping("/deleteById")
    public Response<?> deleteById(@ApiParam(value = "删除ID", required = true) @RequestParam String id) {
        permissionService.deleteOnCascadeById(id);
        return Response.ok().build();
    }

    @ApiOperation(value = "查询详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/infoById")
    @JsonBodys({@JsonBody(type = Permission.class, includes = {"id", "code", "name", "type", "url", "description",
        "enabled", "order", "route", "icon"})})
    public Response<Permission> infoById(@ApiParam(value = "权限ID", required = true) @RequestParam String id) {
        return Response.ok(permissionService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "查询权限列表，树状数据结构")
    @PostMapping("/rootsTree")
    @JsonBodys({@JsonBody(type = Permission.class, includes = {"id", "code", "name", "type", "url", "description",
        "enabled", "children", "order", "route", "icon"})})
    public Response<List<Permission>> rootsTree() {
        List<Permission> roots = permissionService.findRootsTree();
        if (ICollections.hasNoElements(roots)) {
            return Response.error(ErrType.ENTITY_QUERY_LIST_ERROR, "无可分配的权限列表").build();
        }
        return Response.ok(roots);
    }
}
