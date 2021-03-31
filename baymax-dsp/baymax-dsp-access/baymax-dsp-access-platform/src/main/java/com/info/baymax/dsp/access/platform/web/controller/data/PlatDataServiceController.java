package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.web.base.BaseEntityController;
import com.info.baymax.dsp.data.consumer.constant.DataServiceStatus;
import com.info.baymax.dsp.data.consumer.constant.DataServiceType;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.dataset.entity.core.FlowExecution;
import com.info.baymax.dsp.data.dataset.service.core.FlowExecutionService;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/service")
@Api(tags = "数据管理： 数据服务接口", value = "数据服务接口")
public class PlatDataServiceController implements BaseEntityController<DataService> {

    @Autowired
    private DataServiceService dataServiceService;
    @Autowired
    private FlowExecutionService flowExecutionService;

    @Override
    public BaseEntityService<DataService> getBaseEntityService() {
        return dataServiceService;
    }

    @ApiOperation(value = "启用停用", notes = "服务启用停用接口")
    @PostMapping("/updateStatus")
    public Response<?> updateStatus(@ApiParam(value = "启用停用对象，传ID和状态值", required = true) @RequestBody DataService t) {
        DataService dataService = dataServiceService.selectByPrimaryKey(t.getId());
        dataService.setStatus(t.getStatus());
        if (t.getExpiredTime() != null && t.getExpiredTime() > 0) {
            dataService.setExpiredTime(t.getExpiredTime());
        }
        dataService.setLastModifiedTime(new Date());
        dataService.setLastModifier(SaasContext.getCurrentUsername());
        if (t.getStatus() == DataServiceStatus.SERVICE_STATUS_DEPLOYED) {
            dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_READY);
        } else if (t.getStatus() == DataServiceStatus.SERVICE_STATUS_STOPPED) {
            dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_TO_STOP);
        }
        dataServiceService.update(dataService);
        return Response.ok().build();
    }

    /**
     * 数据服务一但生成就不支持修改了，如果后期允许修改涉及到很多属性的置空
     */
    @Override
    public Response<DataService> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam Long id) {
        DataService dataService = dataServiceService.selectByPrimaryKey(id);
        if (dataService.getType() == DataServiceType.SERVICE_TYPE_PULL) {
            dataService.setExecutedTimes(null);
            dataService.setFailedTimes(null);
            dataService.setLastExecutedTime(null);
            dataService.setIsRunning(null);
            dataService.setJobInfo(null);
        } else if (dataService.getType() == DataServiceType.SERVICE_TYPE_PUSH) {
            dataService.setUrl(null);
            dataService.setPath(null);
        }
        return Response.ok(dataService);
    }

    @ApiOperation(value = "查找Execution", notes = "多条件查询Execution")
    @PostMapping("/tasklist/{flowId}")
    public Response<IPage<FlowExecution>> query(@PathVariable String flowId, @RequestBody ExampleQuery query) {
        // @formatter:off
        query = ExampleQuery
            .builder(query)
            .fieldGroup(FieldGroup.builder()
                .andEqualTo("flowId", flowId)
                .andEqualTo("tenantId", SaasContext.getCurrentTenantId())
            );
        // @formatter:on
        return Response.ok(flowExecutionService.selectPage(query));
    }

    @ApiOperation(value = "根据ID列表, 导出数据服务信息", notes = "根据ID列表，导出数据服务信息")
    @GetMapping(value = "/exportExcelByIds")
    public Mono<Void> exportDataServiceByIds(@ApiParam(value = "数据服务ids", required = true) @RequestParam("ids") List<Long> ids,
                                             @ApiParam(value = "数据服务类型，申请服务type为0，订阅服务type为1", required = true) @RequestParam("type") Integer type,
                                        ServerHttpResponse response) throws IOException {
        String filePath = dataServiceService.exportDataServiceByIds(ids, type);

        ZeroCopyHttpOutputMessage outputMessage = (ZeroCopyHttpOutputMessage) response;
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filePath.substring(filePath.lastIndexOf("/") + 1));
        response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        response.getHeaders().setAcceptCharset(Arrays.asList(Charset.forName("utf-8")));

        File target = new File(filePath);
        return outputMessage.writeWith(target, 0, target.length());
    }

    @ApiOperation(value = "pull导入数据服务信息", notes = "通过Excel文件导入")
    @PostMapping(value = "/importExcelByPull", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<List<String>> importExcelByPull(
                                              @ApiParam(value = "消费者id", required = true) @RequestParam("custId") String custId,
                                              @ApiParam(value = "PULL操作配置,关联CustApp获取接入配置信息", required = true) @RequestParam("custAppId") Long custAppId,
                                              @RequestPart("file") FilePart file) {
        String dirPath = new File(".").getAbsolutePath().substring(0, new File(".").getAbsolutePath().length() - 1) + "/files/import/";
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String filePath = dirPath + file.filename();
        File targetFile = new File(filePath);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        file.transferTo(targetFile);

        try {
            List<String> resultList = dataServiceService.importExcelByPull(custId, targetFile, custAppId);
            return Response.ok(resultList);
        } catch (Exception e) {
            return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage()).build();
        }
    }

    @ApiOperation(value = "push导入数据服务信息", notes = "通过Excel文件导入")
    @PostMapping(value = "/importExcelByPush", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<List<String>> importExcelByPush(
            @ApiParam(value = "消费者id", required = true) @RequestParam("custId") String custId,
            @ApiParam(value = "PUSH操作相关配置,关联CustDataResource获取push信息", required = true) @RequestParam("custDataSourceId") Long custDataSourceId,
            @ApiParam(value = "数据源表名称", required = true) @RequestParam("custTableName") String custTableName,
            @RequestPart("file") FilePart file) {
        String dirPath = new File(".").getAbsolutePath().substring(0, new File(".").getAbsolutePath().length() - 1) + "/files/import/";
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String filePath = dirPath + file.filename();
        File targetFile = new File(filePath);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        file.transferTo(targetFile);

        try {
            List<String> resultList = dataServiceService.importExcelByPush(custId, targetFile, custDataSourceId, custTableName);
            return Response.ok(resultList);
        } catch (Exception e) {
            return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage()).build();
        }
    }
}
