package com.info.baymax.dsp.data.platform.service;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.platform.entity.DataService;

import java.util.List;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/18
 */
public interface DataServiceEntityService extends BaseEntityService<DataService> {

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
}
