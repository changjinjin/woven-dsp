package com.info.baymax.dsp.data.platform.service;

import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.common.persistence.service.tree.id.TreeIdableExampleQueryService;
import com.info.baymax.dsp.data.platform.entity.DataCategory;

import java.util.List;

public interface DataCategoryService
    extends BaseEntityService<DataCategory>, TreeIdableExampleQueryService<Long, DataCategory> {

    /**
     * 排序
     *
     * @param list 排序的集合，按顺序存放
     * @return 排序结果
     */
    int sort(List<DataCategory> list);

    /**
     * 移动
     *
     * @param ids    移动的目录ID
     * @param destId 移动目标目录ID
     * @return 移动结果
     */
    int move(Long[] ids, Long destId);

    /**
     * 查询目录树
     *
     * @param rootId 根目录ID
     * @return 目录树
     */
    List<DataCategory> tree(Long rootId);

}
