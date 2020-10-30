package com.info.baymax.common.comp.base.crud;

import com.info.baymax.common.entity.id.Idable;
import com.info.baymax.common.queryapi.result.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RestController
public interface BaseSaveController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "添加记录", notes = "新建数据记录，新建时主键为空值")
    @PostMapping("/")
    default Response<?> save(@ApiParam(value = "待新建记录", required = true) @RequestBody @NotNull @Valid T t) {
        getBaseIdableAndExampleQueryService().saveOrUpdate(t);
        return Response.ok(t.getId());
    }

}
