package com.info.baymax.common.persistence.jpa.page;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ApiModel
@Deprecated
public class Page<T> implements Serializable {
    private static final long serialVersionUID = -8853851041929515638L;

    @ApiModelProperty("数据列表")
    private List<T> content;
    @ApiModelProperty("是否分页")
    private boolean pageable = true;
    @ApiModelProperty("数据总条数")
    private long totalElements;
    @ApiModelProperty("总页数")
    private int totalPages;
    @ApiModelProperty("本页数据量")
    private int size;
    @ApiModelProperty("总数据量")
    private long number;
    @ApiModelProperty("偏移量")
    private long numberOfElements;
    @ApiModelProperty("是否为空")
    private boolean empty;
    @ApiModelProperty("是否第一页")
    private boolean first;
    @ApiModelProperty("是否最后一页")
    private boolean last;

    public Page(int pageNum, int pageSize, long totalCount, List<T> content) {
        this.content = content;
        this.totalElements = totalCount;
        this.size = pageSize;
        this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
        this.number = totalElements;
        this.numberOfElements = pageNum * pageSize;
        this.first = pageNum <= 1;
        this.last = pageNum >= totalPages;
        this.empty = totalElements <= 0;
    }

    public List<T> getContent() {
        if (content == null) {
            return Lists.newArrayList();
        }
        return content;
    }
}
