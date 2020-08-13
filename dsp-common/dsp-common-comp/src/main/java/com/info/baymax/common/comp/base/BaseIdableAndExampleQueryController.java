package com.info.baymax.common.comp.base;

import com.info.baymax.common.entity.id.Idable;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.service.BaseIdableAndExampleQueryService;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@RestController
public interface BaseIdableAndExampleQueryController<ID extends Serializable, T extends Idable<ID>> {

    BaseIdableAndExampleQueryService<ID, T> getBaseIdableAndExampleQueryService();

    @ApiOperation(value = "添加记录", notes = "新建数据记录，新建时主键为空值")
    @PostMapping("/save")
    default Response<?> save(@ApiParam(value = "待新建记录", required = true) @RequestBody @Valid T t) {
        getBaseIdableAndExampleQueryService().saveOrUpdate(t);
        return Response.ok(t.getId());
    }

    @ApiOperation(value = "修改记录", notes = "编辑数据记录，编辑时根据主键查找修改记录，ID值不能为空，当只需要更新部分字段时可只传部分字段的值，其他字段值为空，或者传全部字段")
    @PostMapping("/update")
    default Response<?> update(@ApiParam(value = "待编辑记录", required = true) @RequestBody @Valid T t) {
        getBaseIdableAndExampleQueryService().saveOrUpdate(t);
        return Response.ok().build();
    }

    @ApiOperation(value = "单个删除", notes = "根据ID每次删除一条数据，ID不能为空")
    @GetMapping("/deleteById")
    default Response<?> deleteById(@ApiParam(value = "删除ID", required = true) @RequestParam ID id) {
        getBaseIdableAndExampleQueryService().deleteById(id);
        return Response.ok().build();
    }

    @ApiOperation(value = "批量删除", notes = "根据ID批量删除数据，数据集不能为空")
    @GetMapping("/deleteByIds")
    default Response<?> deleteByIds(@ApiParam(value = "ID列表", required = true) @RequestParam @NotEmpty ID[] ids) {
        getBaseIdableAndExampleQueryService().deleteByIds(ids);
        return Response.ok().build();
    }

    @ApiOperation(value = "分页查询", notes = "根据条件分页查询数据，复杂的查询条件需要构建一个ExampleQuery对象")
    @PostMapping("/page")
    default Response<IPage<T>> page(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        return Response.ok(getBaseIdableAndExampleQueryService().selectPage(ExampleQuery.builder(query)));
    }

    @ApiOperation(value = "查询详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/infoById")
    default Response<T> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam ID id) {
        return Response.ok(getBaseIdableAndExampleQueryService().selectByPrimaryKey(id));
    }
}
