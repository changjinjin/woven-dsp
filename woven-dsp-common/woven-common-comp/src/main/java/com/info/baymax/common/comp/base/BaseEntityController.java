package com.info.baymax.common.comp.base;

import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public interface BaseEntityController<T extends BaseEntity> {

    BaseEntityService<T> getBaseEntityService();

    @ApiOperation(value = "添加记录")
    @PostMapping("save")
    @ResponseBody
    default Response<?> save(@ApiParam(value = "待新建记录") @RequestBody T t) {
        getBaseEntityService().saveOrUpdate(t);
        return Response.ok();
    }

    @ApiOperation(value = "修改记录")
    @PostMapping("update")
    @ResponseBody
    default Response<?> update(@ApiParam(value = "待编辑记录") @RequestBody T t) {
        getBaseEntityService().saveOrUpdate(t);
        return Response.ok();
    }

    @ApiOperation(value = "单个删除")
    @GetMapping("deleteById")
    @ResponseBody
    default Response<?> deleteById(@ApiParam(value = "删除ID", required = true) @RequestParam Long id) {
        getBaseEntityService().deleteByPrimaryKey(id);
        return Response.ok();
    }

    @ApiOperation(value = "批量删除")
    @GetMapping("deleteByIds")
    @ResponseBody
    default Response<?> deleteByIds(@ApiParam(value = "ID列表", required = true) @RequestParam List<Long> ids) {
        getBaseEntityService().deleteByPrimaryKeys(ids);
        return Response.ok();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("page")
    @ResponseBody
    default Response<IPage<T>> page(@ApiParam(value = "查询条件") @RequestBody ExampleQuery query) {
        return Response.ok(getBaseEntityService().selectPage(ExampleQuery.builder(query)));
    }

    @ApiOperation(value = "查询详情")
    @GetMapping("infoById")
    @ResponseBody
    default Response<T> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam Long id) {
        return Response.ok(getBaseEntityService().selectByPrimaryKey(id));
    }
}
