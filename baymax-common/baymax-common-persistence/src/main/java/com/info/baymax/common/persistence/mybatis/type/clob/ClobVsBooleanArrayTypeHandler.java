package com.info.baymax.common.persistence.mybatis.type.clob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Clob VS Boolean Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-28 17:27
 */
public class ClobVsBooleanArrayTypeHandler extends AbstractClobTypeHandler<Boolean[]> {

    @Override
    public String translate2Str(Boolean[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Boolean[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Boolean::valueOf).toArray(Boolean[]::new);
    }

}
