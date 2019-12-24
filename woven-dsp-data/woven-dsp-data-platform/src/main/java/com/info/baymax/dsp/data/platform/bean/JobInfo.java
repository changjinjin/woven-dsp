package com.info.baymax.dsp.data.platform.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: haijun
 * @Date: 2019/12/24 18:55
 */
@Data
public class JobInfo implements Serializable {
    private static final long serialVersionUID = 6554550660525788143L;
    private String flowId;
    private String scheduleId;
    private String executionId;
}
