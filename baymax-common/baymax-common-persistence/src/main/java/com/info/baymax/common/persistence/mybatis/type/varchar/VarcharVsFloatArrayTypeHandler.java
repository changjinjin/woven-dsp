package com.info.baymax.common.persistence.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Varchar VS Float Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-28 13:53
 */
public class VarcharVsFloatArrayTypeHandler extends AbstractVarcharTypeHandler<Float[]> {

    @Override
    public String translate2Str(Float[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Float[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Float::valueOf).toArray(Float[]::new);
    }
}
