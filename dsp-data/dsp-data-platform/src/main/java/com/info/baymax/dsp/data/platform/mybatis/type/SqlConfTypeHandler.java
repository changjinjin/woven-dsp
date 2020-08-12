package com.info.baymax.dsp.data.platform.mybatis.type;

import com.info.baymax.common.mybatis.type.varchar.AbstractVarcharTypeHandler;
import com.info.baymax.common.utils.JsonUtils;
import com.info.baymax.dsp.data.platform.entity.SqlConf;
import org.apache.commons.lang3.StringUtils;

public class SqlConfTypeHandler extends AbstractVarcharTypeHandler<SqlConf> {

    @Override
    public String translate2Str(SqlConf t) {
        return JsonUtils.toJson(t);
    }

    @Override
    public SqlConf translate2Bean(String result) {
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        return JsonUtils.fromJson(result, SqlConf.class);
    }
}
