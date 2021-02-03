package com.info.baymax.common.persistence.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Varchar VS Long Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-28 13:52
 */
public class VarcharVsLongArrayTypeHandler extends AbstractVarcharTypeHandler<Long[]> {

    @Override
    public String translate2Str(Long[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Long[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Long::valueOf).toArray(Long[]::new);
    }
}
