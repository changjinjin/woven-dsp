package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.dsp.access.dataapi.data.DataReadException;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class MapEntityListHandler extends AbstractListHandler<MapEntity> {

    private final RowProcessor convert;

    public MapEntityListHandler() {
        this(new BasicRowProcessor());
    }

    public MapEntityListHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    @Override
    protected MapEntity handleRow(ResultSet rs) throws SQLException {
        return MapEntity.from(this.convert.toMap(rs));
        // return buildRecord(rs, Charset.defaultCharset().name());
    }

    protected final byte[] EMPTY_CHAR_ARRAY = new byte[0];

    protected MapEntity buildRecord(ResultSet rs, String mandatoryEncoding) throws SQLException {
        try {
            MapEntity record = MapEntity.build();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnNumber = metaData.getColumnCount();
            for (int i = 1; i <= columnNumber; i++) {
                String columnName = metaData.getColumnName(i);
                switch (metaData.getColumnType(i)) {

                    case Types.CHAR:
                    case Types.NCHAR:
                    case Types.VARCHAR:
                    case Types.LONGVARCHAR:
                    case Types.NVARCHAR:
                    case Types.LONGNVARCHAR:
                        String rawData;
                        if (StringUtils.isBlank(mandatoryEncoding)) {
                            rawData = rs.getString(i);
                        } else {
                            rawData = new String((rs.getBytes(i) == null ? EMPTY_CHAR_ARRAY : rs.getBytes(i)),
                                mandatoryEncoding);
                        }
                        record.add(columnName, rawData);
                        break;

                    case Types.CLOB:
                    case Types.NCLOB:
                        record.add(columnName, rs.getString(i));
                        break;

                    case Types.SMALLINT:
                    case Types.TINYINT:
                    case Types.INTEGER:
                    case Types.BIGINT:
                        record.add(columnName, rs.getString(i));
                        break;

                    case Types.NUMERIC:
                    case Types.DECIMAL:
                        record.add(columnName, rs.getString(i));
                        break;

                    case Types.FLOAT:
                    case Types.REAL:
                    case Types.DOUBLE:
                        record.add(columnName, rs.getString(i));
                        break;

                    case Types.TIME:
                        record.add(columnName, rs.getTime(i));
                        break;

                    // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
                    case Types.DATE:
                        if (metaData.getColumnTypeName(i).equalsIgnoreCase("year")) {
                            record.add(columnName, rs.getInt(i));
                        } else {
                            record.add(columnName, rs.getDate(i));
                        }
                        break;

                    case Types.TIMESTAMP:
                        record.add(columnName, rs.getTimestamp(i));
                        break;

                    case Types.BINARY:
                    case Types.VARBINARY:
                    case Types.BLOB:
                    case Types.LONGVARBINARY:
                        record.add(columnName, rs.getBytes(i));
                        break;

                    // warn: bit(1) -> Types.BIT 可使用BoolColumn
                    // warn: bit(>1) -> Types.VARBINARY 可使用BytesColumn
                    case Types.BOOLEAN:
                    case Types.BIT:
                        record.add(columnName, rs.getBoolean(i));
                        break;

                    case Types.NULL:
                        String stringData = null;
                        if (rs.getObject(i) != null) {
                            stringData = rs.getObject(i).toString();
                        }
                        record.add(columnName, stringData);
                        break;

                    default:
                        throw new DataReadException(String.format(
                            "您的配置文件中的列配置信息有误. 因为DataX 不支持数据库读取这种字段类型. 字段名:[%s], 字段名称:[%s], 字段Java类型:[%s]. 请尝试使用数据库函数将其转换datax支持的类型 或者不同步该字段 .",
                            columnName, metaData.getColumnType(i), metaData.getColumnClassName(i)));
                }
            }
            return record;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

}