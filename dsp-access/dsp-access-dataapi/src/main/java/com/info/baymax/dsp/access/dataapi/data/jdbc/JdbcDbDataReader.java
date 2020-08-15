package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.info.baymax.access.dataapi.api.MapEntity;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.query.parameters.SqlQuery;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import com.info.baymax.dsp.access.dataapi.data.Supported;

public interface JdbcDbDataReader extends Supported<StorageConf> {

    /**
     * 根据sql模板查询
     *
     * @param conf  数据源配置信息
     * @param query 查询条件
     * @return 查询的结果
     * @throws Exception
     */
    IPage<MapEntity> readBySql(StorageConf conf, SqlQuery query) throws Exception;

}
