package com.info.baymax.dsp.access.dataapi.data;

import com.info.baymax.common.page.IPage;

import org.springframework.core.Ordered;

/**
 * 数据读取接口
 *
 * @param <T> 返回数据类型
 * @author jingwei.yang
 * @date 2020年6月22日 上午10:17:13
 */
public interface DataReader<T> extends Ordered {

    /**
     * 根据配置信息判别是否支持数据读取
     *
     * @param conf 配置信息
     * @return 是否支持
     */
    boolean supports(StorageConf conf);

    /**
     * 分页查询数据
     *
     * @param conf          配置信息
     * @param includeFields 查询的字段列表
     * @param pageable      分页信息
     * @return 分页数据
     * @throws DataReadException
     */
    IPage<T> read(StorageConf conf, Query query) throws DataReadException;

    @Override
    default int getOrder() {
        return 0;
    }

}
