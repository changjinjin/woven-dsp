package com.info.baymax.dsp.data.platform.mybatis.type;

import com.info.baymax.common.mybatis.type.enums.IntegerValuableEnumTypeHandler;
import com.info.baymax.dsp.data.platform.bean.TransferType;

public class TransferTypeEnumTypeHandler extends IntegerValuableEnumTypeHandler<TransferType> {

    public TransferTypeEnumTypeHandler(Class<TransferType> type) {
        super(type);
    }

}
