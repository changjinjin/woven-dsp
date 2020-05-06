package com.info.baymax.dsp.access.dataapi.config;

import com.info.baymax.dsp.access.dataapi.web.request.PullRequest;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import com.info.baymax.dsp.data.platform.bean.GrowthType;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.entity.DataTransferRecord;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import com.info.baymax.dsp.data.platform.service.DataTransferRecordService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.lang.reflect.Method;

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

    @Autowired
    private DataCustAppService dataCustAppService;
    @Autowired
    private DataServiceEntityService dataServiceEntityService;
    @Autowired
    private DataTransferRecordService dataTransferRecordService;

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
            doLog(args, startTime, endTime, resultCode);
        }
    }

    /**
     * 执行日志记录功能
     *
     * @param args 接口参数列表
     */
    private void doLog(Object[] args, long startTime, long endTime, int resultCode) {
        try {
            PullRequest request = (PullRequest) args[0];
            String accessKey = request.getAccessKey();
            boolean encrypted = request.isEncrypted();
            long timestamp = request.getTimestamp();
            Long dataServiceId = request.getDataServiceId();
            int offset = request.getOffset();
            int size = request.getSize();
            DataCustApp app = dataCustAppService.selectByAccessKeyNotNull(accessKey);
            if (app == null) {
                return;
            }

            DataService dataService = dataServiceEntityService.selectByPrimaryKey(dataServiceId);
            if (dataService == null) {
                return;
            }

            dataTransferRecordService.insertSelective(DataTransferRecord.pull(app.getName(), app.getTenantId(),
                app.getOwner(), app.getCustId(), app.getCustName(), app.getId(), app.getName(), dataServiceId,
                dataService.getName(), startTime, endTime, endTime - startTime, GrowthType.LIST, "", (long) offset,
                (long) size, resultCode));
            log.debug(
                "some body pull data with params:accessKey={},encrypted={},timestamp={},dataServiceId={},offset={},size={}.",
                accessKey, encrypted, timestamp, dataServiceId, offset, size);
        } catch (Exception e) {
            log.error("record data pull info error", e);
        }
    }
}
