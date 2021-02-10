package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.config.base.BaseEntityController;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.platform.bean.ApplyConfiguration;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/16 14:44 管理员针对消费者的申请记录进行操作
 */
@Slf4j
@Api(tags = "数据管理：管理员审批相关接口", description = "管理员审批相关接口")
@RestController
@RequestMapping("/application")
public class PlatDataApplicationController implements BaseEntityController<DataApplication> {

    @Autowired
    private DataApplicationService dataApplicationService;
    @Autowired
    private DataServiceService dataServiceEntityService;
    @Autowired
    private DataCustAppService dataCustAppService;

    @Override
    public BaseEntityService<DataApplication> getBaseEntityService() {
        return dataApplicationService;
    }

    @Override
    public Response<DataApplication> infoById(Long id) {
        DataApplication t = dataApplicationService.selectByPrimaryKey(id);
        if (t.getCustAppId() != null) {
            t.setDataCustApp(dataCustAppService.selectByPrimaryKey(t.getCustAppId()));
        }
        return Response.ok(t);
    }

    @ApiOperation(value = "审批消费者申请记录")
    @PostMapping("/approval/{status}")
    public Response<?> approvalDataApplication(@PathVariable Integer status, @RequestBody DataService dataService) throws Exception {
        DataApplication dataApplication = dataApplicationService.selectByPrimaryKey(dataService.getApplicationId());
        dataApplication.setStatus(status);
        dataApplication.setLastModifiedTime(new Date());
        dataApplication.setLastModifier(SaasContext.getCurrentUsername());
        dataApplication.setDescription(dataService.getDescription());
        dataApplication.setAuditMind(dataService.getAuditMind());
        dataApplicationService.update(dataApplication);

        if (status == 1) {
            try {
                // 审批通过把DataApplication中的pull/push配置信息写入DataService
                ApplyConfiguration applyConfiguration = new ApplyConfiguration();
                applyConfiguration.setCustAppId(dataApplication.getCustAppId());
                applyConfiguration.setCustDataSourceId(dataApplication.getCustDataSourceId());
                applyConfiguration.setCustDataSourceName(dataApplication.getCustDataSourceName());
                applyConfiguration.setCustTableName(dataApplication.getCustTableName());
                applyConfiguration.setDataResId(dataApplication.getDataResId());
                applyConfiguration.setDataResName(dataApplication.getDataResName());
                applyConfiguration.setServiceMode(dataApplication.getServiceMode());
                applyConfiguration.setApplicationId(dataApplication.getId());
                applyConfiguration.setApplicationName(dataApplication.getName());
                applyConfiguration.setCustId(dataApplication.getOwner());
                applyConfiguration.setCustName(dataApplication.getCreator());
                dataService.setApplyConfiguration(applyConfiguration);
                dataService.setCustId(dataApplication.getOwner());
                dataService.setOwner(SaasContext.getCurrentUserId());//不能存customer的id,存管理员id
                dataService.setFieldMappings(dataApplication.getFieldMappings());
                dataService.setDataResId(dataApplication.getDataResId());

                Map<String, String> otherConfiguration = dataApplication.getOtherConfiguration();
                dataService.setServiceConfiguration(otherConfiguration);
                if (otherConfiguration != null && otherConfiguration.get("scheduleType") != null) {
                    dataService.setScheduleType(otherConfiguration.get("scheduleType"));
                }

                List<DataService> records = dataServiceEntityService.findAllByTenantIdAndName(dataService.getTenantId(),
                    dataApplication.getName());
                if (records != null && records.size() > 0) {
                    dataService.setName(dataApplication.getName() + "_" + getDateStr("yyyyMMddHHmmss"));
                }
                dataServiceEntityService.saveOrUpdate(dataService);
            } catch (Exception e) {
                log.error("approval and save dataservice exception :", e);
                dataApplicationService.updateDataApplicationStatus(dataService.getApplicationId(), 0);
                log.info("restore dataApplication status success :{}, {}", dataApplication.getId(), 0);
                return Response.error(ErrType.ENTITY_SAVE_ERROR, "save dataService error").build();
            }
        }
        return Response.ok(dataService.getId());
    }

    private String getDateStr(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

}
