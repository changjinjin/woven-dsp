package com.info.baymax.dsp.access.dataapi.data.hbase;

import java.util.List;

import com.info.baymax.dsp.access.dataapi.data.StorageConf;

import lombok.Getter;
import lombok.Setter;

/**
 * hbase 存储配置
 * 
 * <pre>
 *  {
 *      "namespace": "default",
 *      "table": "hbase_sink_0412_6"
 *      "columnsKey": "LOCAL",
 *      "columns": "rowKey:LOCAL,col2:ID,col3:jw,col3:jason,col6:j6",
 *      "encryptKey": "",
 *      "encryptColumns": "",
 *      "columnsItems": 4,
 *      "columnsColumns": "",
 *      "columnsColumns1": "col2:ID",
 *      "columnsColumns2": "col3:jw",
 *      "columnsColumns3": "col3:jason",
 *      "columnsColumns4": "col6:j6",
 *      "pathMode": "exact",
 *      "format": "csv",
 *      "isSingle": false,
 *  }
 * </pre>
 * 
 * @author jingwei.yang
 * @date 2020年9月8日 下午3:59:56
 */
@Setter
@Getter
public class HbaseStorageConf extends StorageConf {
	private static final long serialVersionUID = 106952647390719639L;

	/**
	 * 表名称
	 */
	private String table;

	/**
	 * 主键列
	 */
	private String columnsKey;

	/**
	 * 列信息
	 */
	private String columns;// : "columns:id,rowKey:key,columns:age,columns:desc,columns:dateStr,columns:city"

	/**
	 * 列族数量
	 */
	private String columnsItems;

	/**
	 * 列族，单列族只有一个，多列族存储多个，保证顺序
	 */
	private List<String> columnFamilies;

	/**
	 * 加密key
	 */
	private String encryptKey;

	/**
	 * 加密列
	 */
	private String encryptColumns;

	/**
	 * 命名空间
	 */
	private String namespace = "default";

	/**
	 * 路径模式
	 */
	private String pathMode = "exact";

}
