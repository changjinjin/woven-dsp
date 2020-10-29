package com.info.baymax.dsp.access.dataapi.data;

import com.google.common.collect.Maps;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;

import java.util.Map;

/**
 * 查询解析器
 *
 * @param <S>  存儲配置信息
 * @param <RQ> RecordQuery 记录查询条件
 * @param <R>  RecordQuery 解析生成查询条件
 * @param <AQ> AggQuery 聚合查询条件
 * @param <A>  AggQuery 解析生成查询条件
 * @author jingwei.yang
 * @date 2020年6月28日 下午12:23:27
 */
public interface QueryParser<S extends StorageConf, RQ extends RecordQuery, R, AQ extends AggQuery, A> {
    public static final Map<Class<? extends QueryParser<?, ?, ?, ?, ?>>, QueryParser<?, ?, ?, ?, ?>> cache = Maps
        .newHashMap();

    @SuppressWarnings("unchecked")
    static <Q extends QueryParser<?, ?, ?, ?, ?>> Q getInstance(Class<Q> clazz)
        throws InstantiationException, IllegalAccessException {
        QueryParser<?, ?, ?, ?, ?> parser = null;
        synchronized (cache) {
            parser = cache.get(clazz);
            if (parser == null) {
                parser = clazz.newInstance();
                cache.put(clazz, parser);
            }
        }
        return (Q) parser;
    }

    /**
     * 根据RecordQuery信息解析查询的参数
     *
     * @param storageConf 存储配置信息
     * @param query       通用记录查询条件
     * @return 对应引擎的记录查询条件
     */
    R parseRecordQuery(S storageConf, RecordQuery query) throws Exception;

    /**
     * 根据存储信息和RecordQuery查询参数构建Query对象
     *
     * @param storageConf 存储配置信息
     * @param query       查询参数信息
     * @return 对应引擎的RecordQuery实现
     * @throws Exception
     */
    RQ convertRecordQuery(S storageConf, RecordQuery query) throws Exception;

    /**
     * 根据AggQuery信息解析查询的参数
     *
     * @param storageConf 存储配置信息
     * @param query       通用聚合查询条件
     * @return 对应引擎的聚合查询条件
     * @throws Exception
     */
    A parseAggQuery(S storageConf, AggQuery query) throws Exception;

    /**
     * 根据AggQuery信息解析查询的参数
     *
     * @param storageConf 存储配置信息
     * @param query       通用聚合查询条件
     * @return 对应引擎的AggQuery实现
     * @throws Exception
     */
    AQ convertAggQuery(S storageConf, AggQuery query) throws Exception;

}
