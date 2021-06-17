package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.core.exception.ControllerException;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.web.base.BaseEntityController;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.platform.bean.ApplyConfiguration;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "消费端：消费者应用配置管理", description = "消费者应用配置管理")
@RestController
@RequestMapping("/custApp")
public class DataCustAppController implements BaseEntityController<DataCustApp> {

    @Autowired
    private DataCustAppService dataCustAppService;
    @Autowired
    private DataServiceService dataServiceEntityService;

    @Override
    public BaseEntityService<DataCustApp> getBaseEntityService() {
        return dataCustAppService;
    }

    @Override
    public Response<IPage<DataCustApp>> page(ExampleQuery query) {
        query = ExampleQuery.builder(query);
        query.fieldGroup().andEqualTo("owner", SaasContext.getCurrentUserId());
        return BaseEntityController.super.page(query);
    }

    @ApiOperation(value = "单个删除", notes = "根据ID每次删除一条数据，ID不能为空")
    @GetMapping("/deleteById")
    public Response<Integer> deleteById(@ApiParam(value = "删除ID", required = true) @RequestParam Long id) {
        DataCustApp dataCustApp = dataCustAppService.selectByPrimaryKey(id);
        int type = 0; //申请服务
        List<DataService> list = dataServiceEntityService.selectByCustIdAndType(dataCustApp.getCustId(), type);
        if(ICollections.hasElements(list)){
            for(DataService dataService : list){
                DataService dataServiceNew = dataServiceEntityService.selectByPrimaryKey(dataService.getId());
                ApplyConfiguration applyConfigurationNew = dataServiceNew.getApplyConfiguration();
                Long custAppId = applyConfigurationNew.getCustAppId();
                if(id.longValue() == custAppId.longValue()){
                    throw new ControllerException(ErrType.ENTITY_DELETE_ERROR,
                            String.format("The access configuration has associated with data service ID %s and cannot be delete", dataService.getId()));
                }
            }
        }
        return Response.ok(dataCustAppService.deleteById(id));
    }
}
