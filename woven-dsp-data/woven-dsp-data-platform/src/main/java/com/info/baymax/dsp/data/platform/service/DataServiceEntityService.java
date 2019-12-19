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
     * @param type
     * @param status
     * @param isRunning
     * @return
     */
    List<DataService> querySpecialDataService(Integer type, Integer status, Integer isRunning);

    /**
     * 将待执行的服务更新为正在执行状态
     * @param type
     * @param status
     * @param isRunning
     */
    void updateDataServiceToRunning(Integer type, Integer status, Integer isRunning);

    void updateDataServiceToRunning(Long id);

    /**
     * 服务重启时恢复之前正在running的服务
     */
    void recoverDataService();

    /**
     * 开机启动先更新执行过的once类型的服务
     */
    void updateFinishedDataService();
}
