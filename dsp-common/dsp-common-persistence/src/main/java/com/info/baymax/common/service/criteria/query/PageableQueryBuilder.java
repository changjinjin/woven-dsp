package com.info.baymax.common.service.criteria.query;

import com.info.baymax.common.page.IPageable;

/**
 * 分页查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface PageableQueryBuilder<B extends PageableQueryBuilder<B>> {

    /**
     * 是否分页
     *
     * @param pageable 是否分页
     * @return this builder
     */
    B paged(boolean pageable);

    /**
     * 开启分页
     *
     * @return this builder
     */
    default B paged() {
        return paged(true);
    }

    /**
     * 关闭分页
     *
     * @return this builder
     */
    default B unpaged() {
        return paged(false);
    }

    /**
     * 设置分页信息
     *
     * @return this builder
     */
    B pageable(IPageable pageable);

    /**
     * 指定分页当前页码
     *
     * @param pageNum 页码
     * @return this builder
     */
    B pageNum(int pageNum);

    /**
     * 指定分页页长
     *
     * @param pageSize 页长
     * @return this builder
     */
    B pageSize(int pageSize);

    /**
     * 分页
     *
     * @param pageNum  页码
     * @param pageSize 页长
     * @return this builder
     */
    default B page(int pageNum, int pageSize) {
        return pageable(IPageable.page(pageNum, pageSize));
    }

    /**
     * 分页
     *
     * @param offset 偏移量
     * @param limit  页长
     * @return this builder
     */
    default B offset(int offset, int limit) {
        return pageable(IPageable.offset(offset, limit));
    }

    /**
     * 清空Pageable条件
     *
     * @return this builder
     */
    B clearPageable();

}
