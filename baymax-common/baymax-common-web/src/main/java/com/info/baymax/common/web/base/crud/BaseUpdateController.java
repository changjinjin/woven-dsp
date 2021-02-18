package com.info.baymax.common.web.base.crud;

import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.persistence.entity.gene.Idable;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RestController
public interface BaseUpdateController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "修改记录", notes = "编辑数据记录，编辑时根据主键查找修改记录，ID值不能为空，当只需要更新部分字段时可只传部分字段的值，其他字段值为空，或者传全部字段")
    @PutMapping("/{id}")
    default Response<?> update(@ApiParam(value = "记录ID", required = true) @PathVariable("id") ID id,
                               @ApiParam(value = "更新数据", required = true) @RequestBody @NotNull @Valid T t) {
        t.setId(id);
        getBaseIdableAndExampleQueryService().saveOrUpdate(t);
        return Response.ok().build();
    }

}
