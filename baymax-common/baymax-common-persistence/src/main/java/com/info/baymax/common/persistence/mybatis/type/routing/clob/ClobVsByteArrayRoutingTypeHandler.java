package com.info.baymax.common.persistence.mybatis.type.routing.clob;

import com.info.baymax.common.persistence.mybatis.type.clob.AbstractClobTypeHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Clob VS Byte Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:44
 */
public class ClobVsByteArrayRoutingTypeHandler extends AbstractClobTypeHandler<Byte[]> {

    @Override
    public String translate2Str(Byte[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Byte[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).map(Byte::valueOf).toArray(Byte[]::new);
    }
}
