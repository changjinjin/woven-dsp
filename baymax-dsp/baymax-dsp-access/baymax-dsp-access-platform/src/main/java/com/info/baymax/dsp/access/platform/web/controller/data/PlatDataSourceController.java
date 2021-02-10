package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.config.base.BaseEntityController;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: haijun
 * @Date: 2020/2/14 0014 17:54
 */
@RestController
@RequestMapping("/dss")
@Api(tags = "平台管理端: 数据源管理接口", value = "数据源管理接口")
public class PlatDataSourceController implements BaseEntityController<CustDataSource>{
    @Autowired
    private CustDataSourceService custDataSourceService;

    @Override
    public BaseEntityService<CustDataSource> getBaseEntityService() {
        return custDataSourceService;
    }

    @ApiOperation(value = "查询数据源详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/{id}")
    public Response<CustDataSource> queryById(@ApiParam(value = "记录ID", required = true) @PathVariable Long id) {
        return Response.ok(custDataSourceService.selectByPrimaryKey(id));
    }

}
