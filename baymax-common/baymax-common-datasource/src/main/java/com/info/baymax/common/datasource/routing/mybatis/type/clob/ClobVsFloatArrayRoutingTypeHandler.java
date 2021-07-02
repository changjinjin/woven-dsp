package com.info.baymax.common.datasource.routing.mybatis.type.clob;

import com.info.baymax.common.persistence.mybatis.type.clob.AbstractClobTypeHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Clob VS Float Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:45
 */
public class ClobVsFloatArrayRoutingTypeHandler extends AbstractClobTypeHandler<Float[]> {

    @Override
    public String translate2Str(Float[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Float[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Float::valueOf).toArray(Float[]::new);
    }
}
