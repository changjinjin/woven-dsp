package com.info.baymax.common.comp.base;

import com.info.baymax.common.entity.id.Idable;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.result.Response;
import com.info.baymax.common.service.BaseIdableAndExampleQueryService;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RestController
public interface BaseCrudController<ID extends Serializable, T extends Idable<ID>> {

    BaseIdableAndExampleQueryService<ID, T> getBaseIdableAndExampleQueryService();

    @ApiOperation(value = "添加记录", notes = "新建数据记录，新建时主键为空值")
    @PostMapping("/")
    default Response<?> save(@ApiParam(value = "待新建记录", required = true) @RequestBody @NotNull @Valid T t) {
        getBaseIdableAndExampleQueryService().saveOrUpdate(t);
        return Response.ok(t.getId());
    }

    @ApiOperation(value = "修改记录", notes = "编辑数据记录，编辑时根据主键查找修改记录，ID值不能为空，当只需要更新部分字段时可只传部分字段的值，其他字段值为空，或者传全部字段")
    @PutMapping("/{id}")
    default Response<?> update(@ApiParam(value = "记录ID", required = true) @PathVariable("id") ID id,
                               @ApiParam(value = "更新数据", required = true) @RequestBody @NotNull @Valid T t) {
        t.setId(id);
        getBaseIdableAndExampleQueryService().saveOrUpdate(t);
        return Response.ok().build();
    }

    @ApiOperation(value = "删除记录", notes = "根据ID删除一条记录")
    @DeleteMapping("/{id}")
    default Response<?> deleteById(@ApiParam(value = "记录ID", required = true) @PathVariable("id") ID id) {
        getBaseIdableAndExampleQueryService().deleteById(id);
        return Response.ok().build();
    }

    @ApiOperation(value = "分页查询", notes = "根据条件分页查询数据，复杂的查询条件需要构建一个ExampleQuery对象")
    @PostMapping("/page")
    default Response<IPage<T>> page(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        return Response.ok(getBaseIdableAndExampleQueryService().selectPage(ExampleQuery.builder(query)));
    }

    @ApiOperation(value = "查询详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/{id}")
    default Response<T> infoById(@ApiParam(value = "记录ID", required = true) @PathVariable("id") ID id) {
        return Response.ok(getBaseIdableAndExampleQueryService().selectByPrimaryKey(id));
    }
}
