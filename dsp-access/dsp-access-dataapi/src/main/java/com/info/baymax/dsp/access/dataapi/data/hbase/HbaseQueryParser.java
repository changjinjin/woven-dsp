package com.info.baymax.dsp.access.dataapi.data.hbase;

import org.apache.hadoop.hbase.client.Get;

import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.dsp.access.dataapi.data.QueryParser;

public class HbaseQueryParser implements QueryParser<HbaseStorageConf, RecordQuery, Get, AggQuery, Get> {

	@Override
	public Get parseRecordQuery(HbaseStorageConf conf, RecordQuery query) throws Exception {
		// return RecordQuerySql.builder(convertRecordQuery(conf, query));
		return null;
	}

	@Override
	public RecordQuery convertRecordQuery(HbaseStorageConf conf, RecordQuery query) throws Exception {
		return query.table(conf.getTable());
	}

	@Override
	public Get parseAggQuery(HbaseStorageConf conf, AggQuery query) throws Exception {
		// return AggQuerySql.builder(convertAggQuery(conf, query));
		return null;
	}

	@Override
	public AggQuery convertAggQuery(HbaseStorageConf conf, AggQuery query) throws Exception {
		return query.table(conf.getTable());
	}
}
