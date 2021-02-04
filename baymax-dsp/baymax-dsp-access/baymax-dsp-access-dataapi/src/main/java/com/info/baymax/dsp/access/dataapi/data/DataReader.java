package com.info.baymax.dsp.access.dataapi.data;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import org.springframework.core.Ordered;

/**
 * 数据读取接口
 *
 * @param <R> record类型
 * @param <A> agg结果类型
 * @author jingwei.yang
 * @date 2020年6月22日 上午10:17:13
 */
public interface DataReader<R, A> extends Ordered, Supported<StorageConf> {

    /**
     * 数据记录查询数据
     *
     * @param conf  配置信息
     * @param query 查询条件
     * @return 分页数据
     * @throws Exception
     */
    IPage<R> readRecord(StorageConf conf, RecordQuery query) throws Exception;

    /**
     * 聚合结果查询数据
     *
     * @param conf  配置信息
     * @param query 聚合查询条件
     * @return 聚合查询的结果
     * @throws Exception
     */
    IPage<A> readAgg(StorageConf conf, AggQuery query) throws Exception;

    @Override
    default int getOrder() {
        return 0;
    }

}
