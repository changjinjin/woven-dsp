package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.platform.entity.DataCategory;
import com.info.baymax.dsp.data.platform.service.DataCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "数据管理：数据目录管理", description = "数据目录管理")
@RestController
@RequestMapping("/cate")
@Validated
public class DataCategoryController implements BaseEntityController<DataCategory> {

    @Autowired
    private DataCategoryService dataCategoryService;

    @Override
    public BaseEntityService<DataCategory> getBaseEntityService() {
        return dataCategoryService;
    }

    @Override
    public Response<?> deleteById(Long id) {
        dataCategoryService.deleteById(id);
        return Response.ok().build();
    }

    @ApiOperation(value = "根据ID批量删除", hidden = true)
    @Override
    public Response<?> deleteByIds(Long[] ids) {
        throw new ControllerException(ErrType.GONE);
    }

    @ApiOperation(value = "分页查询", hidden = true)
    @Override
    public Response<IPage<DataCategory>> page(ExampleQuery query) {
        throw new ControllerException(ErrType.GONE);
    }

    @ApiOperation(value = "目录排序")
    @PostMapping("sort")
    public Response<?> sort(@ApiParam(value = "待排序记录，只传id属性即可", required = true) @RequestBody @NotEmpty List<DataCategory> list) {
        dataCategoryService.sort(list);
        return Response.ok().build();
    }

    @ApiOperation(value = "移动目录")
    @PostMapping("move")
    public Response<?> move(//
                            @ApiParam(value = "移动目录", required = true) @RequestBody @NotEmpty Long[] ids, //
                            @ApiParam(value = "目标目录", required = true) @RequestParam @NotNull Long destId//
    ) {
        dataCategoryService.move(ids, destId);
        return Response.ok().build();
    }

    @ApiOperation(value = "目录列表")
    @PostMapping("tree")
    public Response<List<DataCategory>> tree(
        @ApiParam(value = "根目录ID", defaultValue = "0") @RequestParam(defaultValue = "0") Long rootId) {
        return Response.ok(dataCategoryService.tree(rootId));
    }

}
