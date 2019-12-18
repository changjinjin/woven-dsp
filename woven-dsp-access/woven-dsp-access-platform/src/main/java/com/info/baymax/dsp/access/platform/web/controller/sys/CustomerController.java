package com.info.baymax.dsp.access.platform.web.controller.sys;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.comp.serialize.annotation.JsonBody;
import com.info.baymax.common.comp.serialize.annotation.JsonBodys;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "系统管理：消费者管理", description = "消费者管理")
@RestController
@RequestMapping("/cust")
public class CustomerController implements BaseEntityController<Customer> {

    @Autowired
    private CustomerService consumerService;

    @Override
    public BaseEntityService<Customer> getBaseEntityService() {
        return consumerService;
    }

    @ApiOperation(value = "分页查询消费者信息")
    @PostMapping("page")
    @ResponseBody
    @JsonBodys({@JsonBody(type = Customer.class, excludes = "password")})
    public Response<IPage<Customer>> page(@ApiParam(value = "查询条件") @RequestBody ExampleQuery query) {
        return BaseEntityController.super.page(query);
    }

    @ApiOperation(value = "查询消费者信息")
    @GetMapping("infoById")
    @ResponseBody
    @JsonBodys({@JsonBody(type = Customer.class, excludes = "password")})
    public Response<Customer> infoById(@ApiParam(value = "消费者ID", required = true) @RequestParam Long id) {
        return BaseEntityController.super.infoById(id);
    }

}
