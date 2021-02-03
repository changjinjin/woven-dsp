package com.info.baymax.common.persistence.mybatis.type.blob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Blob VS Float Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2017年11月10日 下午12:44:32
 */
public class BlobVsFloatArrayTypeHandler extends AbstractBlobTypeHandler<Float[]> {

    @Override
    public String translate2Str(Float[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Float[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Float::valueOf).toArray(Float[]::new);
    }
}
