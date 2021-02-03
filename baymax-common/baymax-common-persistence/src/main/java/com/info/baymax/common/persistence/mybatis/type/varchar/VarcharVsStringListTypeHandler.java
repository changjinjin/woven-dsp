package com.info.baymax.common.persistence.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Varchar VS String List TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-28 13:51
 */
public class VarcharVsStringListTypeHandler extends AbstractVarcharTypeHandler<List<String>> {

    @Override
    public String translate2Str(List<String> t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public List<String> translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_REGEX)).collect(Collectors.toList());
    }
}
