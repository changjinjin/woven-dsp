package com.info.baymax.dsp.data.consumer.constant;

/**
 * @Author: haijun
 * @Date: 2019/12/19 11:23
 * 0 待部署,1 部署成功, 2 服务停止, 3 服务过期
 */
public class DataServiceStatus {
    //0 待部署,1 部署成功, 2 服务停止, 3 服务已过期
    public static final Integer SERVICE_STATUS_READY = 0;
    public static final Integer SERVICE_STATUS_DEPLOYED = 1;
    public static final Integer SERVICE_STATUS_STOPPED = 2;
    public static final Integer SERVICE_STATUS_EXPIRED = 3;
}
