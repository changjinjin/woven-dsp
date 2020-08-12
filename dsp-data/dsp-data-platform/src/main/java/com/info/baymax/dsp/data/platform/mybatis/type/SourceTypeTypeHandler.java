package com.info.baymax.dsp.data.platform.mybatis.type;

import com.info.baymax.dsp.data.platform.entity.SourceType;
import org.apache.ibatis.type.EnumTypeHandler;

public class SourceTypeTypeHandler extends EnumTypeHandler<SourceType> {

    public SourceTypeTypeHandler() {
        super(SourceType.class);
    }
}
