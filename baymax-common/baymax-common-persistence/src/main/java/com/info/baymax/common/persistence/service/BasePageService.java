package com.info.baymax.common.persistence.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.info.baymax.common.core.page.IPageable;
import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

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

    default <E> List<E> converPageToList(List<E> list) {
        if (list != null && list instanceof Page) {
            return Lists.newArrayList(list);
        }
        return list;
    }
}
