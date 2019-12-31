package com.info.baymax.dsp.data.platform.mybatis.mapper.type;

import com.info.baymax.common.mybatis.type.clob.AbstractClobTypeHandler;
import com.info.baymax.common.mybatis.type.clob.ClobVsObjectTypeHandler;
import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.platform.bean.JobInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * Clob VS String Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:47
 */
public class ClobVsJobInfoTypeHandler extends ClobVsObjectTypeHandler<JobInfo> {
}
