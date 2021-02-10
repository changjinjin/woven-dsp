package com.info.baymax.common.config.base.crud;

import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.persistence.entity.gene.Enabled;
import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.service.EnabledService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@RestController
public interface EnabledController<ID extends Serializable, E extends Serializable, T extends Enabled<E> & Idable<ID>> {

    EnabledService<ID, E, T> getEnabledService();

    @ApiOperation(value = "修改启用状态", notes = "根据ID和enabled值修改启用停用状态")
    @PutMapping("/enabled/{enabled}/{id}")
    default Response<?> updateEnabled(@ApiParam(value = "启用停用状态值", required = true) @PathVariable("enabled") E enabled,
                                      @ApiParam(value = "记录ID", required = true) @PathVariable("id") ID id) {
        getEnabledService().updateEnabled(id, enabled);
        return Response.ok().build();
    }

    @ApiOperation(value = "批量修改启用状态", notes = "根据ID集合和enabled值批量修改启用停用状态")
    @PostMapping("/enabled/{enabled}")
    default Response<?> updateEnabled(@ApiParam(value = "启用停用状态值", required = true) @PathVariable("enabled") E enabled,
                                      @ApiParam(value = "记录ID集合", required = true) @RequestBody @NotEmpty(message = "Entity ids could not be null or empty.") List<ID> ids) {
        getEnabledService().updateEnabled(ids, enabled);
        return Response.ok().build();
    }
}
