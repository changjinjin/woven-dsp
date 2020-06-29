package com.info.baymax.dsp.access.dataapi.data;

/**
 * 查询解析器
 * 
 * @author jingwei.yang
 * @date 2020年6月28日 下午12:23:27
 * @param <Q> Query实现
 * @param <T> 解析后的查询条件
 */
public interface QueryParser<S extends StorageConf, Q extends Query, T> {

	/**
	 * 根据query信息解析查询的参数
	 * 
	 * @param storageConf 存储配置信息
	 * @param query       通用查询条件
	 * @return 对应的引擎的查询条件
	 */
	T parse(S storageConf, Query query) throws Exception;

	/**
	 * 根据存储参数和查询参数构建Query对象
	 * 
	 * @param storageConf 存储配置信息
	 * @param query       查询参数信息
	 * @return 指定类型的Query对象
	 * @throws Exception
	 */
	Q parseQuery(S storageConf, Query query) throws Exception;

}
