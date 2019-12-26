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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/16 14:44 管理员针对消费者的申请记录进行操作
 */
@Api(tags = "数据管理：管理员审批相关接口", description = "管理员审批相关接口")
@RestController
@RequestMapping("/application")
public class PlatDataApplicationController implements BaseEntityController<DataApplication> {
    @Autowired
    private DataApplicationService dataApplicationService;

    @Autowired
    private DataServiceEntityService dataServiceEntityService;

    @Value("${dataapi.url.list}")
    private String dataApiUrl;

    @Value("${dataapi.path}")
    private String dataApiPath;

    @Value("${dataapi.params}")
    private String queryParams;

    @Override
    public BaseEntityService<DataApplication> getBaseEntityService() {
        return dataApplicationService;
    }

    @ApiOperation(value = "审批消费者申请记录")
    @PostMapping("/approval/{status}")
    public Response<?> approvalDataApplication(@PathVariable Integer status, @RequestBody DataService dataService)
        throws Exception {
        dataApplicationService.updateDataApplicationStatus(dataService.getApplicationId(), status);
        if (status == 1) {
            DataApplication dataApplication = dataApplicationService.selectByPrimaryKey(dataService.getApplicationId());
            dataService.setCustId(dataApplication.getOwner());
            if (dataService.getType() == 0) {   //pull 服务, 配置接口信息
                dataService.setUrl(dataApiUrl);
                dataService.setPath(dataApiPath);

                Map<String, String> pullConfig = new HashMap<>();
                for (String param : queryParams.split(",")) {
                    pullConfig.put(param.split(":")[0], param.split(":")[1]);
                }
                dataService.setPullConfiguration(pullConfig);
            }
            dataServiceEntityService.saveOrUpdate(dataService);
        }
        return Response.ok();
    }

}
