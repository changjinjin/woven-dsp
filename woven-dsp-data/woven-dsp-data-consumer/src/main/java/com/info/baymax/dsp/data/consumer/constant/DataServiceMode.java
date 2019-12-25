package com.info.baymax.dsp.data.consumer.constant;

/**
 * @Author: haijun
 * @Date: 2019/12/25 17:05
 * 针对pull、push支持的服务方式
 */
public class DataServiceMode {
    /**
     * push service mode
     */
    public static final Integer total_mode = 0;//全量
    public static final Integer increment_mode = 1;//增量

    /**
     * pull service
     */
    public static final Integer list_mode = 2;//列表

}
