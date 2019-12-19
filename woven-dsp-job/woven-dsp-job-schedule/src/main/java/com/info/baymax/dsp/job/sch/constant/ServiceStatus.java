package com.info.baymax.dsp.job.sch.constant;

/**
 * @Author: haijun
 * @Date: 2019/12/19 11:23
 */
public class ServiceStatus {
    //0 待部署,1 部署成功, 2 停止服务, 3 执行完成
    public static final Integer SERVICE_STATUS_READY = 0;
    public static final Integer SERVICE_STATUS_DEPLOYED = 1;
    public static final Integer SERVICE_STATUS_STOPPED = 2;
    public static final Integer SERVICE_STATUS_FINISHED = 3;
}
