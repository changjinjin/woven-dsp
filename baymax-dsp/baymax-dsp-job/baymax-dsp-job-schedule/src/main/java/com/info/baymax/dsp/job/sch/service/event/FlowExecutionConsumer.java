package com.info.baymax.dsp.job.sch.service.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.JsonUtils;
import com.info.baymax.dsp.data.consumer.constant.DataServiceStatus;
import com.info.baymax.dsp.data.consumer.constant.ScheduleJobStatus;
import com.info.baymax.dsp.data.consumer.constant.ScheduleType;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;

/**
 * monitor flow execution status change, and notify DataService.
 */
@Component
@RocketMQMessageListener(
        topic = "${rocketmq.dsp-dataset.topic:dsp_dataset}",
        consumerGroup = "${woven.dsp.group:woven_dsp_group}",
        consumeMode = ConsumeMode.CONCURRENTLY,
        selectorExpression = "*"
)
public class FlowExecutionConsumer implements RocketMQListener<MessageExt> {

    private final Logger logger = LoggerFactory.getLogger(FlowExecutionConsumer.class);

    @Autowired
    DataServiceService dataServiceEntityService;

    @Autowired
    DataResourceService dataResourceService;

    @Override
    public void onMessage(MessageExt msg) {
        String messageBody = new String(msg.getBody());
        logger.debug("get msg:" + messageBody);
        try {
            Dataset dataset = JsonUtils.fromJson(messageBody, Dataset.class);
            String datasetId = dataset.getId();
            List<DataResource> dataResourceList = dataResourceService.selectList(
                    ExampleQuery.builder().fieldGroup(FieldGroup.builder().andEqualTo("datasetId", datasetId)));

            List<Long> dataResIdList = new ArrayList<>();
            for (DataResource dataResource : dataResourceList) {
                Long dataResId = dataResource.getId();
                dataResIdList.add(dataResId);
            }
            logger.debug("dataResIdList size: " + dataResIdList.size());

            if (dataResIdList.size() > 0) {
                ExampleQuery dataServiceQuery = ExampleQuery.builder().fieldGroup(
                        FieldGroup.builder()
                        .andIn("dataResId", dataResIdList.toArray())
                        .andEqualToIfNotNull("scheduleType", ScheduleType.SCHEDULER_TYPE_EVENT)
                        .andEqualToIfNotNull("status", DataServiceStatus.SERVICE_STATUS_DEPLOYED)
                        .andNotEqualToIfNotNull("isRunning", ScheduleJobStatus.JOB_STATUS_RUNNING)
                );

                List<DataService> dataServiceList = dataServiceEntityService.selectList(dataServiceQuery);

                for (DataService dataService : dataServiceList) {
                    dataService.setIsRunning(ScheduleJobStatus.JOB_STATUS_READY);
                    dataServiceEntityService.saveOrUpdate(dataService);
                }
                logger.info("send ready dataService size: " + dataServiceList.size());
            }
        } catch (Exception e) {
            logger.error("handle the message error,message:{} , bodyStr:{}", msg, messageBody);
            logger.error("handle the message Exception: ", e);
        }
    }
}
