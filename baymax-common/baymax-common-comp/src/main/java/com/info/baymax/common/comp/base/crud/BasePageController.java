package com.info.baymax.common.comp.base.crud;

import com.info.baymax.common.entity.id.Idable;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.result.Response;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public interface BasePageController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "分页查询", notes = "根据条件分页查询数据，复杂的查询条件需要构建一个ExampleQuery对象")
    @PostMapping("/page")
    default Response<IPage<T>> page(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        return Response.ok(getBaseIdableAndExampleQueryService().selectPage(ExampleQuery.builder(query)));
    }

}
