package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.platform.entity.DataCategory;
import com.info.baymax.dsp.data.platform.service.DataCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "数据管理：数据目录管理", description = "数据目录管理")
@RestController
@RequestMapping("/cate")
public class DataCategoryController implements BaseEntityController<DataCategory> {

    @Autowired
    private DataCategoryService dataCategoryService;

    @Override
    public BaseEntityService<DataCategory> getBaseEntityService() {
        return dataCategoryService;
    }

    @ApiOperation(value = "单个删除")
    @GetMapping("/deleteById")
    @ResponseBody
    @Override
    public Response<?> deleteById(@ApiParam(value = "删除ID", required = true) @RequestParam Long id) {
        if (id == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "删除记录ID不能为空");
        }
        dataCategoryService.deleteById(id);
        return Response.ok();
    }

    @ApiOperation(value = "批量删除", hidden = true)
    @GetMapping("/deleteByIds")
    @ResponseBody
    @Override
    public Response<?> deleteByIds(@ApiParam(value = "ID列表", required = true) @RequestParam Long[] ids) {
        throw new ControllerException(ErrType.INTERNAL_SERVER_ERROR, "不支持此接口。");
    }

    @ApiOperation(value = "分页查询", hidden = true)
    @GetMapping("/page")
    @ResponseBody
    @Override
    public Response<IPage<DataCategory>> page(ExampleQuery query) {
        throw new ControllerException(ErrType.INTERNAL_SERVER_ERROR, "不支持此接口。");
    }

    @ApiOperation(value = "目录排序")
    @PostMapping("sort")
    @ResponseBody
    public Response<?> sort(@ApiParam(value = "待排序记录，只传id属性即可") @RequestBody List<DataCategory> list) {
        if (ICollections.hasNoElements(list)) {
            throw new ControllerException(ErrType.BAD_REQUEST, "排序列表不能为空");
        }
        dataCategoryService.sort(list);
        return Response.ok();
    }

    @ApiOperation(value = "移动目录")
    @PostMapping("move")
    @ResponseBody
    public Response<?> move(//
                            @ApiParam(value = "移动目录", required = true) @RequestBody Long[] ids, //
                            @ApiParam(value = "目标目录", required = true) @RequestParam Long destId//
    ) {
        if (ids == null || ids.length == 0) {
            throw new ControllerException(ErrType.BAD_REQUEST, "移动的目录不能为空");
        }
        if (destId == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "移动目标目录不能为空");
        }
        dataCategoryService.move(ids, destId);
        return Response.ok();
    }

    @ApiOperation(value = "目录列表")
    @PostMapping("tree")
    @ResponseBody
    public Response<List<DataCategory>> tree(
        @ApiParam(value = "根目录ID", defaultValue = "0") @RequestParam(defaultValue = "0") Long rootId) {
        return Response.ok(dataCategoryService.tree(rootId));
    }

}
