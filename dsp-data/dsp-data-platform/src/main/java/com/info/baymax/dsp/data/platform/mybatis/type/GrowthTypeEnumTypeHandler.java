package com.info.baymax.dsp.data.platform.mybatis.type;

import com.info.baymax.common.mybatis.type.enums.IntegerValuableEnumTypeHandler;
import com.info.baymax.dsp.data.platform.bean.GrowthType;

public class GrowthTypeEnumTypeHandler extends IntegerValuableEnumTypeHandler<GrowthType> {

    public GrowthTypeEnumTypeHandler(Class<GrowthType> type) {
        super(type);
    }
}
