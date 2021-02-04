package com.info.baymax.common.comp.base.crud;

import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.persistence.entity.gene.Idable;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public interface BaseInfoController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "查询详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/{id}")
    default Response<T> infoById(@ApiParam(value = "记录ID", required = true) @PathVariable("id") ID id) {
        return Response.ok(getBaseIdableAndExampleQueryService().selectByPrimaryKey(id));
    }

}
