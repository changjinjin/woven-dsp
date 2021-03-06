package com.info.baymax.common.persistence.mybatis.type.blob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Blob VS Short Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:42
 */
public class BlobVsShortArrayTypeHandler extends AbstractBlobTypeHandler<Short[]> {

    @Override
    public String translate2Str(Short[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Short[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Short::valueOf).toArray(Short[]::new);
    }
}
