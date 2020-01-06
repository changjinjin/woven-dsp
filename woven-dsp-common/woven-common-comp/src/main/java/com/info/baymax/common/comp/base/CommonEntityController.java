package com.info.baymax.common.comp.base;

import com.info.baymax.common.entity.base.CommonEntity;
import com.info.baymax.common.entity.base.CommonEntityService;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@RestController
public interface CommonEntityController<ID extends Serializable, T extends CommonEntity<ID>> {

    CommonEntityService<ID, T> getCommonEntityService();

    @ApiOperation(value = "添加记录", notes = "新建数据记录，新建时主键为空值")
    @PostMapping("/save")
    @ResponseBody
    default Response<?> save(@ApiParam(value = "待新建记录") @RequestBody T t) {
        if (t == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "保存对象不能为空");
        }
        getCommonEntityService().saveOrUpdate(t);
        return Response.ok(t.getId());
    }

    @ApiOperation(value = "修改记录", notes = "编辑数据记录，编辑时根据主键查找修改记录，ID值不能为空，当只需要更新部分字段时可只传部分字段的值，其他字段值为空，或者传全部字段")
    @PostMapping("/update")
    @ResponseBody
    default Response<?> update(@ApiParam(value = "待编辑记录") @RequestBody T t) {
        if (t == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "编辑对象不能为空");
        }
        getCommonEntityService().saveOrUpdate(t);
        return Response.ok();
    }

    @ApiOperation(value = "单个删除", notes = "根据ID每次删除一条数据，ID不能为空")
    @GetMapping("/deleteById")
    @ResponseBody
    default Response<?> deleteById(@ApiParam(value = "删除ID", required = true) @RequestParam ID id) {
        if (id == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "删除记录ID不能为空");
        }
        getCommonEntityService().deleteByPrimaryKey(id);
        return Response.ok();
    }

    @ApiOperation(value = "批量删除", notes = "根据ID批量删除数据，数据集不能为空")
    @GetMapping("/deleteByIds")
    @ResponseBody
    default Response<?> deleteByIds(@ApiParam(value = "ID列表", required = true) @RequestParam ID[] ids) {
        if (ids == null || ids.length == 0) {
            throw new ControllerException(ErrType.BAD_REQUEST, "删除记录ID不能为空");
        }
        getCommonEntityService().deleteByPrimaryKeys(ids);
        return Response.ok();
    }

    @ApiOperation(value = "分页查询", notes = "根据条件分页查询数据，复杂的查询条件需要构建一个ExampleQuery对象")
    @PostMapping("/page")
    @ResponseBody
    default Response<IPage<T>> page(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        if (query == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询条件不能为空");
        }
        return Response.ok(getCommonEntityService().selectPage(ExampleQuery.builder(query)));
    }

    @ApiOperation(value = "查询详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/infoById")
    @ResponseBody
    default Response<T> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam ID id) {
        if (id == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询记录ID不能为空");
        }
        return Response.ok(getCommonEntityService().selectByPrimaryKey(id));
    }
}
