package com.info.baymax.common.persistence.mybatis.type.routing.clob;

import com.info.baymax.common.persistence.mybatis.type.clob.AbstractClobTypeHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clob VS String List TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:48
 */
public class ClobVsStringListRoutingTypeHandler extends AbstractClobTypeHandler<List<String>> {

    @Override
    public String translate2Str(List<String> t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public List<String> translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).collect(Collectors.toList());
    }
}
