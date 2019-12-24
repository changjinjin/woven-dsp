package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: haijun
 * @Date: 2019/12/16 14:44 管理员针对消费者的申请记录进行操作
 */
@Api(tags = "管理员审批相关接口", description = "管理员审批相关接口")
@RestController
@RequestMapping("/application")
public class PlatDataApplicationController implements BaseEntityController<DataApplication> {
    @Autowired
    private DataApplicationService dataApplicationService;

    @Autowired
    private DataServiceEntityService dataServiceEntityService;

    @Override
    public BaseEntityService<DataApplication> getBaseEntityService() {
        return dataApplicationService;
    }

    @ApiOperation(value = "审批消费者申请记录")
    @PostMapping("/approval/{status}")
    public Response<?> updateDataApplication(@PathVariable Integer status, @RequestBody DataService dataService)
        throws Exception {
        dataApplicationService.updateDataApplicationStatus(dataService.getApplicationId(), status);
        if (status == 1) {
            dataServiceEntityService.insert(dataService);
        }
        return Response.ok();
    }

}
