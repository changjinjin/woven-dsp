package com.info.baymax.dsp.access.dataapi.config;

import com.info.baymax.common.queryapi.page.IPageable;
import com.info.baymax.data.elasticsearch.entity.DataTransferRecord;
import com.info.baymax.dsp.access.dataapi.api.AggRequest;
import com.info.baymax.dsp.access.dataapi.api.DataRequest;
import com.info.baymax.dsp.access.dataapi.api.RecordRequest;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import com.merce.woven.metrics.report.MetricsReporter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * 接口参数和结果加密解密切面处理
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午11:08:14
 */
@Aspect
@Component
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class DataPullRecordAspect {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private MetricsReporter metricsReporter;
    @Autowired
    private DataCustAppService dataCustAppService;
    @Autowired
    private DataServiceService dataServiceService;
    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    private DatasetService datasetService;

    @Pointcut("execution(* com.info.baymax..*(..)) && @annotation(com.info.baymax.dsp.access.dataapi.config.PullLog)")
    public void joinPointExpression() {
    }

    @Around(value = "joinPointExpression()")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjd.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = pjd.getArgs();
        PullLog pullLog = method.getAnnotation(PullLog.class);
        if (pullLog == null || !pullLog.value()) {
            return pjd.proceed(args);
        }

        long startTime = System.currentTimeMillis();
        int resultCode = 0;
        try {
            return pjd.proceed(args);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultCode = 1;
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            report(args, startTime, endTime, resultCode);
        }
    }

    /**
     * 执行日志记录功能
     *
     * @param args 接口参数列表
     */
    private void report(Object[] args, long startTime, long endTime, int resultCode) {
        try {
            DataRequest<?> request = (DataRequest<?>) args[0];
            String accessKey = request.getAccessKey();
            boolean encrypted = request.isEncrypted();
            long timestamp = request.getTimestamp();
            Long dataServiceId = request.getDataServiceId();
            DataCustApp app = dataCustAppService.selectByAccessKeyNotNull(accessKey);
            if (app == null) {
                return;
            }

            DataService dataService = dataServiceService.selectByPrimaryKey(dataServiceId);
            if (dataService == null) {
                return;
            }

            DataResource dataResource = dataResourceService.selectByPrimaryKey(dataService.getDataResId());
            if (dataResource == null) {
                return;
            }
            Dataset dataset = datasetService.selectByPrimaryKey(dataResource.getDatasetId());
            if (dataset == null) {
                return;
            }

            IPageable pageable = null;
            if (request instanceof AggRequest) {
                pageable = ((AggRequest) request).getQuery().getPageable();
            } else {
                pageable = ((RecordRequest) request).getQuery().getPageable();
            }

            long offset = pageable.getOffset();
            Integer pageSize = pageable.getPageSize();
            DataTransferRecord record = DataTransferRecord.builder()//
                .sid(UUID.randomUUID().toString())//
                .write_time(new Date())//
                .datasetId(dataset.getId())///
                .datasetName(dataset.getName())//
                .custId(app.getCustId())//
                .custName(app.getCustName())//
                .custAppId(app.getId())//
                .custAppName(app.getName())//
                .dataServiceId(dataServiceId)//
                .dataServiceName(dataService.getName())//
                .startTime(startTime)//
                .endTime(endTime)//
                .cost(endTime - startTime)//
                .transferType("PULL")//
                .growthType("LIST")//
                .cursorVal("")//
                .offset((long) offset)//
                .records((long) pageSize)//
                .resultCode(resultCode)//
                .tenantId(app.getTenantId())//
                .owner(app.getOwner()).build();
            metricsReporter.report(DataTransferRecord.TYPE_NAME, appName, startTime, record);
            log.debug(
                "some body pull data with params:accessKey={},encrypted={},timestamp={},dataServiceId={},offset={},size={}.",
                accessKey, encrypted, timestamp, dataServiceId, offset, pageSize);
        } catch (Exception e) {
            log.error("record data pull info error", e);
        }
    }
}
