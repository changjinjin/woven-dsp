package com.info.baymax.common.datasource.routing.mybatis.type.clob;

import com.info.baymax.common.persistence.mybatis.type.clob.AbstractClobTypeHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * Clob VS String Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:47
 */
public class ClobVsStringArrayRoutingTypeHandler extends AbstractClobTypeHandler<String[]> {

    @Override
    public String translate2Str(String[] t) {
        return StringUtils.join(t, DEFAULT_REGEX);
    }

    @Override
    public String[] translate2Bean(String result) {
        return result.split(DEFAULT_REGEX);
    }
}
