package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: haijun
 * @Date: 2019/12/16 11:25 http://xxx.yyy/api/dsp/consumer/
 */
@Api(tags = "消费端: 消费者申请（订阅）记录接口", description = "消费者申请（订阅）记录接口")
@RestController
@RequestMapping("/application")
public class CustDataApplicationController implements BaseEntityController<DataApplication> {

    @Autowired
    private DataApplicationService dataApplicationService;

    @Override
    public BaseEntityService<DataApplication> getBaseEntityService() {
        return dataApplicationService;
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/page")
    @ResponseBody
    public Response<IPage<DataApplication>> page(@ApiParam(value = "查询条件") @RequestBody ExampleQuery query) {
        if (query == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询条件不能为空");
        }
        // 过滤当前消费者的数据
        query = ExampleQuery.builder(query).fieldGroup().andEqualTo("owner", SaasContext.getCurrentUserId()).end();
        return BaseEntityController.super.page(query);
    }

}
