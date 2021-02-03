package com.info.baymax.common.persistence.mybatis.type.blob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Blob VS Long Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:42
 */
public class BlobVsLongArrayTypeHandler extends AbstractBlobTypeHandler<Long[]> {

    @Override
    public String translate2Str(Long[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Long[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Long::valueOf).toArray(Long[]::new);
    }
}
