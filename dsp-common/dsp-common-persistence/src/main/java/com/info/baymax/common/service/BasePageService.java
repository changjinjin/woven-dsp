package com.info.baymax.common.service;

import com.github.pagehelper.PageHelper;
import com.info.baymax.common.page.IPageable;

import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;

@Transactional
public interface BasePageService<T> {
    /**
     * 分页排序
     *
     * @param pageable 分页信息
     */
    default void startPage(IPageable pageable) {
        if (pageable != null) {
            startPage(pageable.getPageNum(), pageable.getPageSize(), pageable.getOrderByClause());
        }
    }

    /**
     * 分页排序
     *
     * @param pageNum       页码
     * @param pageSize      页长
     * @param orderByClause 排序条件
     */
    default void startPage(int pageNum, int pageSize, String orderByClause) {
        PageHelper.startPage(pageNum, pageSize, orderByClause);
    }

    /**
     * 排序
     *
     * @param orderByClause 排序条件
     */
    default void startOrderBy(String orderByClause) {
        if (StringUtils.isNotEmpty(orderByClause)) {
            PageHelper.orderBy(orderByClause);
        }
    }
}
