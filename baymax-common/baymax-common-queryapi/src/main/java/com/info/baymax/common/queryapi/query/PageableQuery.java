package com.info.baymax.common.queryapi.query;

import com.info.baymax.common.core.page.IPageable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@SuppressWarnings("unchecked")
@Getter
public abstract class PageableQuery<T extends PageableQuery<T>> implements PageableQueryBuilder<T> {

    @ApiModelProperty(value = "分页信息，默认不设置分页",required = true)
    protected IPageable pageable = new IPageable(false);

    @Override
    public T paged(boolean pageable) {
        this.pageable.setPageable(pageable);
        return (T) this;
    }

    @Override
    public T pageable(IPageable pageable) {
        this.pageable = pageable;
        return (T) this;
    }

    @Override
    public T pageNum(int pageNum) {
        this.pageable.setPageNum(pageNum);
        return (T) this;
    }

    @Override
    public T pageSize(int pageSize) {
        this.pageable.setPageSize(pageSize);
        return (T) this;
    }

    /***** clear logic ****/
    @Override
    public T clearPageable() {
        if (this.pageable != null && this.pageable.isPageable()) {
            return unpaged();
        }
        return (T) this;
    }
}
