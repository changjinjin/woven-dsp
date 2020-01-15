package com.info.baymax.dsp.data.consumer.constant;

/**
 * @Author: haijun
 * @Date: 2019/12/19 11:23
 * 0 待部署,1 部署成功, 2 停止服务
 */
public class DataServiceStatus {
    //0 待部署,1 部署成功, 2 停止服务, 3 执行结束(针对单次的任务), 4 关联的调度已经停止, 5 服务已经过期
    public static final Integer SERVICE_STATUS_READY = 0;
    public static final Integer SERVICE_STATUS_DEPLOYED = 1;
    public static final Integer SERVICE_STATUS_STOPPED = 2;
    public static final Integer SERVICE_STATUS_FINISHED = 3;
    public static final Integer SERVICE_STATUS_SCHEDULER_STOPPED = 4;
    public static final Integer SERVICE_STATUS_EXPIRED = 5;
}
