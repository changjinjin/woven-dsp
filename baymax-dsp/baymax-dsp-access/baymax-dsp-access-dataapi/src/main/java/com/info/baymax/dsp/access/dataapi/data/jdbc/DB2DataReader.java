package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.dsp.data.consumer.beans.source.DBType;
import org.springframework.stereotype.Component;

/**
 * @Author: jinjin.chang
 * @Date: 2021-05-13 19:35
 */
@Component
public class DB2DataReader extends AbstractJdbcDataReader {

    public DB2DataReader() {
        super(DBType.DB2);
    }
}