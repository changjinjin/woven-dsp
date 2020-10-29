package com.info.baymax.dsp.access.dataapi.data.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseQuerydata {
	static Configuration cfg = HBaseConfiguration.create();
	static {
		cfg.set("hbase.zookeeper.quorum", "192.168.1.95");
		cfg.set("hbase.zookeeper.property.clientPort", "2181");
	}

	public static void queryByRowKey(String tablename, String rowKey) throws IOException {
		HTable table = new HTable(cfg, tablename);
		Get g = new Get(rowKey.getBytes());
		Result rs = table.get(g);

		for (KeyValue kv : rs.raw()) {
			System.out.println("rowkey:        " + new String(kv.getRow()));
			System.out.println("Column Family: " + new String(kv.getFamily()));
			System.out.println("Column       : " + new String(kv.getQualifier()));
			System.out.println("value        : " + new String(kv.getValue()));
		}
	}

	public static void queryByRowKeyFamily(String tablename, String rowKey, String family) throws IOException {
		HTable table = new HTable(cfg, tablename);
		Get g = new Get(rowKey.getBytes());
		g.addFamily(Bytes.toBytes(family));
		Result rs = table.get(g);
		for (KeyValue kv : rs.raw()) {
			System.out.println("rowkey:        " + new String(kv.getRow()));
			System.out.println("Column Family: " + new String(kv.getFamily()));
			System.out.println("Column       : " + new String(kv.getQualifier()));
			System.out.println("value        : " + new String(kv.getValue()));
		}
	}

	public static void queryByRowKeyFamilyColumn(String tablename, String rowKey, String family, String column)
			throws IOException {
		HTable table = new HTable(cfg, tablename);
		Get g = new Get(rowKey.getBytes());
		g.addColumn(family.getBytes(), column.getBytes());

		Result rs = table.get(g);

		for (KeyValue kv : rs.raw()) {
			System.out.println("rowkey:        " + new String(kv.getRow()));
			System.out.println("Column Family: " + new String(kv.getFamily()));
			System.out.println("Column       : " + new String(kv.getQualifier()));
			System.out.println("value        : " + new String(kv.getValue()));
		}
	}

	/*
	 * 查询所有
	 */
	public static void queryAll(String tableName) {
		HTablePool pool = new HTablePool(cfg, 1000);
		try {
			ResultScanner rs = pool.getTable(tableName).getScanner(new Scan());
			for (Result r : rs) {
				System.out.println("rowkey:" + new String(r.getRow()));
				for (KeyValue keyValue : r.raw()) {
					System.out.println(
							"列：" + new String(keyValue.getFamily()) + "     值:" + new String(keyValue.getValue()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("******************************queryall******************************");
		queryAll("wishtest1");
		System.out.println("******************************query by rowkey******************************");
		queryByRowKey("wishTest1", "2");
		System.out.println("******************************query by rowkey family******************************");
		queryByRowKeyFamily("wishTest1", "2", "name");
		System.out.println("******************************query by rowkey family column******************************");
		queryByRowKeyFamilyColumn("wishTest1", "6", "score", "Chinese");
	}
}
