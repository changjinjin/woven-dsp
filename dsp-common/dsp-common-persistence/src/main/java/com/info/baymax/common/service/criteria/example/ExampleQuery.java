package com.info.baymax.common.service.criteria.example;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.info.baymax.common.queryapi.record.AbstractPropertiesQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Example Query ,这个对象的属性可以构造一个{@link com.info.baymax.common.mybatis.mapper.example.Example} 对象
 *
 * @author jingwei.yang
 * @date 2019年6月27日 上午10:08:57
 */
@ApiModel
@Getter
@Setter
@ToString
public class ExampleQuery extends AbstractPropertiesQuery<ExampleQuery> implements ExampleQueryBuilder<ExampleQuery>,
    DistinctQueryBuilder<ExampleQuery>, ForUpdateQueryBuilder<ExampleQuery>, Serializable {
    private static final long serialVersionUID = 4850854513242762929L;

    @ApiModelProperty(value = "查询数据类型", hidden = true)
    private Class<?> entityClass;

    @ApiModelProperty(value = "是否去重，默认false", hidden = true)
    protected boolean distinct;

    @ApiModelProperty(value = "是否锁表，默认false", hidden = true)
    private boolean forUpdate;

    @ApiModelProperty(value = "需要统计的字段名，如：count(id)则该属性为'id'，统计时使用，默认为空", hidden = true)
    private String countProperty;

    @ApiModelProperty(value = "join sql：即子查询语句组合", hidden = true)
    private JoinSql joinSql;

    /*************************************
     * 建造器
     *****************************************/
    /**
     * 创建一个Query的构建器
     *
     * @return 默认的构建器
     */
    public static ExampleQuery builder() {
        return new ExampleQuery();
    }

    /**
     * 创建一个Query的构建器
     *
     * @return 默认的构建器
     */
    public static ExampleQuery builder(ExampleQuery query) {
        return query == null ? new ExampleQuery() : query;
    }

    /**
     * 创建一个Query的构建器
     *
     * @param entityClass 指定查询的数据实体类型
     * @return 默认的构建器
     */
    public static ExampleQuery builder(Class<?> entityClass) {
        return new ExampleQuery(entityClass);
    }

    /*************************************
     * 构造器
     *****************************************/
    public ExampleQuery() {
        super();
    }

    public ExampleQuery(Class<?> entityClass) {
        super();
        this.entityClass = entityClass;
    }

    @Override
    public ExampleQuery distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    @Override
    public ExampleQuery forUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
        return this;
    }

    @Override
    public ExampleQuery countProperty(String countProperty) {
        this.countProperty = countProperty;
        return this;
    }

    @Override
    public ExampleQuery joinSql(JoinSql joinSql) {
        this.joinSql = joinSql;
        return this;
    }

    /***** clear logic ****/

    @Override
    public ExampleQuery clearDistinct() {
        if (this.distinct) {
            this.distinct = false;
        }
        return this;
    }

    @Override
    public ExampleQuery clearForUpdate() {
        if (this.forUpdate) {
            this.forUpdate = false;
        }
        return this;
    }

    @Override
    public ExampleQuery clearCountProperty() {
        if (this.countProperty != null) {
            this.countProperty = null;
        }
        return this;
    }

    @Override
    public ExampleQuery clearJoinSql() {
        this.joinSql = null;
        return this;
    }

    @Override
    public ExampleQuery clear() {
        super.clear();
        clearForUpdate();
        clearCountProperty();
        clearJoinSql();
        return this;
    }

	@Override
	public List<String> getFinalSelectProperties() {
		return Lists.newArrayList(selectProperties);
	}
}
