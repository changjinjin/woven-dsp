package com.info.baymax.dsp.access.dataapi.data.hbase;

import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.common.queryapi.result.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.Engine;
import com.info.baymax.dsp.access.dataapi.data.MapEntityDataReader;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;

public class HbaseDataReader extends MapEntityDataReader {

	protected HbaseDataReader() {
		super(Engine.HBASE);
	}

	@Override
	public IPage<MapEntity> readRecord(StorageConf conf, RecordQuery query) throws Exception {
		return null;
	}

	@Override
	public IPage<MapEntity> readAgg(StorageConf conf, AggQuery query) throws Exception {
		return null;
	}

}
