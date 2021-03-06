package com.info.baymax.common.persistence.mybatis.type.clob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clob VS String Set TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:48
 */
public class ClobVsStringSetTypeHandler extends AbstractClobTypeHandler<Set<String>> {

    @Override
    public String translate2Str(Set<String> t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public Set<String> translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).collect(Collectors.toSet());
    }
}
