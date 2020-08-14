package com.info.baymax.common.queryapi.page;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页参数包装
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午3:55:16
 */
@ApiModel
public class IPageable implements Serializable {
    private static final long serialVersionUID = -4026620219669217389L;

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGENUM = 1;

    /**
     * 默认页长
     */
    public static final int DEFAULT_PAGESIZE = 10;

    /**
     * 是否分页，默认分页
     */
    @ApiModelProperty(value = "是否分页,默认true", allowableValues = "true,false")
    protected boolean pageable = true;

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码，默认1")
    protected int pageNum;

    /**
     * 页长
     */
    @ApiModelProperty(value = "页长，默认10")
    protected int pageSize;

    /**
     * 排序条件，多个条件用“,”分开，如：id asc,name desc（字段名称取数据库中字段名而非实体属性名）
     */
    @ApiModelProperty(value = "排序条件，多个条件用“,”分开，如：id asc,name desc（字段名称取数据库中字段名而非实体属性名）", hidden = true)
    protected String orderByClause;

    /**
     * 描述： 空参构造函数，默认会根据默认页码和默认页长构造分页对象。<br/>
     */
    public IPageable() {
        this(DEFAULT_PAGENUM, DEFAULT_PAGESIZE);
    }

    public IPageable(boolean pageable) {
        this(pageable, DEFAULT_PAGENUM, DEFAULT_PAGESIZE);
    }

    /**
     * 描述： 构造函数，根据页码和页长构造分页对象。<br/>
     *
     * @param pageNum  页码
     * @param pageSize 页长
     */
    public IPageable(int pageNum, int pageSize) {
        this(true, pageNum, pageSize);
    }

    /**
     * 描述： 构造函数,根据页码和页长构造分页对象。<br/>
     *
     * @param pageable 是否分页，默认true
     * @param pageNum  页码
     * @param pageSize 页长
     */
    public IPageable(boolean pageable, int pageNum, int pageSize) {
        this(pageable, pageNum, pageSize, null);
    }

    /**
     * 描述： 构造函数,根据页码和页长构造分页对象。<br/>
     *
     * @param pageable      是否分页，默认true
     * @param pageNum       页码
     * @param pageSize      页长
     * @param orderByClause 排序
     */
    public IPageable(boolean pageable, int pageNum, int pageSize, String orderByClause) {
        this.pageable = pageable;
        if (pageable) {
            if (pageSize <= 0) {
                this.pageSize = DEFAULT_PAGESIZE;
            } else {
                this.pageSize = pageSize;
            }
        } else {
            this.pageSize = Integer.MAX_VALUE;
        }

        if (pageNum < 0) {
            this.pageNum = DEFAULT_PAGENUM;
        } else {
            this.pageNum = pageNum;
        }
        this.orderByClause = orderByClause;
    }

    /**
     * 描述：构造函数， 根据排序信息构造分页对象。<br/>
     *
     * @param orderByClause 排序
     */
    public IPageable(String orderByClause) {
        this(DEFAULT_PAGENUM, DEFAULT_PAGESIZE, orderByClause);
    }

    /**
     * 描述： 构造函数，根据页码和页长构造分页对象。<br/>
     *
     * @param pageNum       页码
     * @param pageSize      页长
     * @param orderByClause 排序
     */
    public IPageable(int pageNum, int pageSize, String orderByClause) {
        this(true, pageNum, pageSize, orderByClause);
    }

    public static IPageable page(int pageNum, int pageSize) {
        return new IPageable(true, pageNum, pageSize);
    }

    public static IPageable offset(int offset, int limit) {
        if (offset < 0) {
            offset = 0;
        }
        if (limit < 0) {
            limit = DEFAULT_PAGESIZE;
        }
        return new IPageable(true, (int) Math.floor((offset + limit) / limit), limit);
    }

    public boolean isPageable() {
        return pageable;
    }

    public void setPageable(Boolean pageable) {
        this.pageable = pageable;
    }

    public int getPageNum() {
        return pageNum <= 0 ? DEFAULT_PAGENUM : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize <= 0 ? DEFAULT_PAGESIZE : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByClause() {
        if (orderByClause == null || orderByClause.isEmpty()) {
            this.orderByClause = initOrderByClause();
        }
        return this.orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public long getOffset() {
        return Math.max(0, (pageNum - 1) * pageSize);
    }

    /**
     * 获取默认的排序字段信息，继承该类需要实现该方法
     *
     * @return 默认的排序字段信息
     */
    public String initOrderByClause() {
        return null;
    }

    @Override
    public String toString() {
        return "IPageable [pageable=" + pageable + ", pageNum=" + pageNum + ", pageSize=" + pageSize
            + ", orderByClause=" + orderByClause + "]";
    }

}
