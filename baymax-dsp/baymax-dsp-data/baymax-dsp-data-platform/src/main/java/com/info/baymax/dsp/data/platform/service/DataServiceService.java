package com.info.baymax.dsp.data.platform.service;

import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.platform.entity.DataService;

import java.io.File;
import java.util.List;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/18
 */
public interface DataServiceService extends BaseEntityService<DataService> {

    /**
     * 根据服务类型,部署状态,运行状态查询
     *
     * @param type
     * @param status
     * @param isRunning
     * @return
     */
    List<DataService> querySpecialDataService(Integer[] type, Integer[] status, Integer[] isRunning);

    /**
     * 根据id更新dataService的isRunning值
     *
     * @param id
     * @param isRunning
     */
    void updateDataServiceRunningStatus(Long id, Integer isRunning);

    /**
     * 服务重启时恢复之前正在running的服务
     */
    void recoverDataService();

    /**
     * 根据dataApplicationId,更新服务状态
     */
    void updateStatusByApplicationId(Long applicationId, Integer status, Integer isRunning);

    /**
     * 根据dataServiceIds,导出数据服务信息记录
     */
    String exportDataServiceByIds(List<Long> ids, Integer type);

    /**
     * 导入excel并返回结果, push导入
     */
    List<String> importExcelByPush(String custId, File targetFile, Long custDataSourceId, String custTableName) throws Exception;

    /**
     * 导入excel并返回结果，pull导入
     */
    List<String> importExcelByPull(String custId, File targetFile, Long custAppId) throws Exception;


    /**
     * 根据用户id和服务类型查询数据服务信息
     */
    List<DataService> selectByCustIdAndType(String custId, int type);
}
