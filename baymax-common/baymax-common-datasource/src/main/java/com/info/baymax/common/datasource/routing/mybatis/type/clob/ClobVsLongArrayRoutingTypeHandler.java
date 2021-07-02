package com.info.baymax.common.datasource.routing.mybatis.type.clob;

import com.info.baymax.common.persistence.mybatis.type.clob.AbstractClobTypeHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Clob VS Long Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:46
 */
public class ClobVsLongArrayRoutingTypeHandler extends AbstractClobTypeHandler<Long[]> {

    @Override
    public String translate2Str(Long[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Long[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Long::valueOf).toArray(Long[]::new);
    }
}
