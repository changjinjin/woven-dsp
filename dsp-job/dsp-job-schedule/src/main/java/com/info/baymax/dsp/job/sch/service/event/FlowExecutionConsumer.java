package com.info.baymax.dsp.job.sch.service.event;

import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.dataset.entity.core.FlowExecution;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * monitor flow execution status change, and notify DataService.
 */
@Component
@RocketMQMessageListener(
        topic = "${rocketmq.flowexecution.topic:flow_execution}",
        consumerGroup = "${woven.dsp.group:woven_dsp_group}",
        consumeMode = ConsumeMode.ORDERLY,
        selectorExpression = "*"
)
public class FlowExecutionConsumer implements RocketMQListener<MessageExt> {

    private final Logger logger = LoggerFactory.getLogger(FlowExecutionConsumer.class);

    @Autowired
    DataServiceEntityService dataServiceEntityService;

    @Autowired
    DataResourceService dataResourceService;

    @Autowired
    DataApplicationService dataApplicationService;

    @Override
    public void onMessage(MessageExt msg) {
        String messageBody = new String(msg.getBody());
        try {
            FlowExecution fe = JsonBuilder.getInstance().fromJson(messageBody, FlowExecution.class);
            logger.info("get flow id:" + fe.getFlowId() + " fid: " + fe.getFid());
        } catch (Exception e) {
            logger.error("handle the message error,message:{} , bodyStr:{}", msg, messageBody);
            logger.error("handle the message Exception: ", e);
        }
    }
}
