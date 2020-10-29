package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.dsp.data.consumer.beans.source.DBType;
import org.springframework.stereotype.Component;

@Component
public class MysqlDataReader extends AbstractJdbcDataReader {
    public MysqlDataReader() {
        super(DBType.Mysql);
    }
}
