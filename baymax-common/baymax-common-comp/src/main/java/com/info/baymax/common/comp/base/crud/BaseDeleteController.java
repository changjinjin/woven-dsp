package com.info.baymax.common.comp.base.crud;

import com.info.baymax.common.entity.gene.Idable;
import com.info.baymax.common.queryapi.result.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public interface BaseDeleteController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "删除记录", notes = "根据ID删除一条记录")
    @DeleteMapping("/{id}")
    default Response<?> deleteById(@ApiParam(value = "记录ID", required = true) @PathVariable("id") ID id) {
        getBaseIdableAndExampleQueryService().deleteById(id);
        return Response.ok().build();
    }

}
