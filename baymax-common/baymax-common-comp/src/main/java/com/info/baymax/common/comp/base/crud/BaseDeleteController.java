package com.info.baymax.common.comp.base.crud;

import com.info.baymax.common.entity.gene.Idable;
import com.info.baymax.common.queryapi.result.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@RestController
public interface BaseDeleteController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "删除记录", notes = "根据ID批量删除记录")
    @DeleteMapping
    default Response<?> deleteByIds(
        @ApiParam(value = "记录ID集合", required = true) @RequestBody @NotEmpty(message = "Delete items could not be empty.") List<ID> ids) {
        getBaseIdableAndExampleQueryService().deleteByIds(ids);
        return Response.ok().build();
    }

}
