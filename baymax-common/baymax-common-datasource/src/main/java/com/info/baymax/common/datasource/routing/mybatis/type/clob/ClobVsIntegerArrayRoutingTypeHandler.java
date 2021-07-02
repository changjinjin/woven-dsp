package com.info.baymax.common.datasource.routing.mybatis.type.clob;

import com.info.baymax.common.persistence.mybatis.type.clob.AbstractClobTypeHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Clob VS Integer Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:45
 */
public class ClobVsIntegerArrayRoutingTypeHandler extends AbstractClobTypeHandler<Integer[]> {

    @Override
    public String translate2Str(Integer[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Integer[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Integer::valueOf).toArray(Integer[]::new);
    }
}
