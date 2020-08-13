package com.info.baymax.data.elasticsearch.service;

import java.util.List;
import java.util.Map;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.data.elasticsearch.entity.DataTransferRecord;

public interface DataTransferRecordService {

    /**
     * 用户访问topN
     *
     * @param start   开始时间
     * @param end     结束时间
     * @param n       前N
     * @param reverse 是否逆序,默认最多的前N
     * @return 排序前N调记录
     * @throws ServiceException
     */
    List<Map<String, Object>> userVisitTopN(long start, long end, int n, boolean reverse) throws ServiceException;

    /**
     * 数据集访问topN
     *
     * @param start   开始时间
     * @param end     结束时间
     * @param n       前N
     * @param reverse 是否逆序,默认最多的前N
     * @return 排序前N调记录
     * @throws ServiceException
     */
    List<Map<String, Object>> datasetVisitTopN(long start, long end, int n, boolean reverse) throws ServiceException;

    /**
     * 用户访问topN
     *
     * @param start   开始时间
     * @param end     结束时间
     * @param custId  消费者ID
     * @param n       前N
     * @param reverse 是否逆序,默认最多的前N
     * @return 排序前N调记录
     * @throws ServiceException
     */
    List<Map<String, Object>> userVisitDatasetTopN(String custId, long start, long end, int n, boolean reverse)
        throws ServiceException;

    /**
     * 数据集访问用户topN
     *
     * @param start     开始时间
     * @param end       结束时间
     * @param datasetId 数据集ID
     * @param n         前N
     * @param reverse   是否逆序,默认最多的前N
     * @return 排序前N调记录
     * @throws ServiceException
     */
    List<Map<String, Object>> datasetVisitUserTopN(String datasetId, long start, long end, int n, boolean reverse)
        throws ServiceException;

    /**
     * 分页查询
     *
     * @param keyword      检索关键字
     * @param transferType 数据获取方式
     * @param growthType   数据增长方式
     * @param custId       消费者ID
     * @param datasetId    数据集ID
     * @param start        开始时间
     * @param end          结束时间
     * @param pageNum      页码
     * @param pageSize     页长
     * @return 分页数据
     * @throws ServiceException
     */
    IPage<DataTransferRecord> queryList(String keyword, String transferType, String growthType, String custId,
                                        String datasetId, long start, long end, int pageNum, int pageSize) throws ServiceException;
}
