package com.info.baymax.common.persistence.mybatis.mapper.aggregation;

import tk.mybatis.mapper.util.Assert;
import tk.mybatis.mapper.util.StringUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 聚合查询条件
 *
 * @author liuchan
 * @author liuzh
 */
public class AggregateCondition implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 聚合属性名称
     */
    private String aggregateProperty;
    /**
     * 聚合属性别名
     */
    private String aggregateAliasName;

    /**
     * groupBy 查询列
     */
    private List<String> groupByProperties;

    /**
     * 存放分组字段的别名，默认无需设置，可以根据需要设置分组字段为新的别名以适配一定的使用场景，如：当以type分组时，查询结果中type值需要映射到newType字段时可以设置别名列表{"type":"newType"}
     */
    private Map<String, String> groupByAliasNames = new HashMap<String, String>();

    /**
     * 聚合函数
     */
    private AggregateType aggregateType;

    public AggregateCondition() {
        this(null, AggregateType.COUNT, null);
    }

    /**
     * 默认查询count计数，不分组
     *
     * @param aggregateProperty 聚合查询属性，不能为空；为保证返回结果key与传入值相同 方法不会去除前后空格
     */
    public AggregateCondition(String aggregateProperty) {
        this(aggregateProperty, AggregateType.COUNT, null);
    }

    /**
     * 默认查询count计数
     *
     * @param aggregateProperty 聚合查询属性，不能为空；为保证返回结果key与传入值相同 方法不会去除前后空格
     * @param groupByProperties 为保证返回结果key与传入值相同 方法不会去除每一项前后空格
     */
    public AggregateCondition(String aggregateProperty, String[] groupByProperties) {
        this(aggregateProperty, AggregateType.COUNT, groupByProperties);
    }

    /**
     * 按指定聚合方法查询，不分组
     *
     * @param aggregateProperty
     * @param aggregateType
     */
    public AggregateCondition(String aggregateProperty, AggregateType aggregateType) {
        this(aggregateProperty, aggregateType, null);
    }

    /**
     * @param aggregateProperty 不能为空，为保证返回结果key与传入值相同 方法不会去除前后空格
     * @param aggregateType
     * @param groupByProperties 为保证返回结果key与传入值相同 方法不会去除每一项前后空格
     */
    public AggregateCondition(String aggregateProperty, AggregateType aggregateType, String[] groupByProperties) {
        this.groupByProperties = new ArrayList<String>();
        // 需要放在propertyMap初始化完成后执行
        aggregateType(aggregateType);
        if (StringUtil.isNotEmpty(aggregateProperty)) {
            aggregateBy(aggregateProperty);
        }
        groupBy(groupByProperties);
    }

    public AggregateCondition(String aggregateProperty, AggregateType aggregateType, String[] groupByProperties,
                              Map<String, String> groupByAliasNames) {
        this(aggregateProperty, aggregateType, groupByProperties);
        this.groupByAliasNames = groupByAliasNames;
    }

    public static AggregateCondition builder() {
        return new AggregateCondition();
    }

    public AggregateCondition groupBy(String... groupByProperties) {
        if (groupByProperties != null && groupByProperties.length > 0) {
            this.groupByProperties.addAll(Arrays.asList(groupByProperties));
        }
        return this;
    }

    public AggregateCondition aggregateBy(String aggregateProperty) {
        Assert.notEmpty(aggregateProperty, "aggregateProperty must have length; it must not be null or empty");
        this.aggregateProperty = aggregateProperty;
        return this;
    }

    public AggregateCondition aliasName(String aggregateAliasName) {
        this.aggregateAliasName = aggregateAliasName;
        return this;
    }

    public AggregateCondition aggregateType(AggregateType aggregateType) {
        Assert.notNull(aggregateType, "aggregateType is required; it must not be null");
        this.aggregateType = aggregateType;
        return this;
    }

    public AggregateCondition groupByPropertyAliasName(String groupByProperty, String groupByPropertyAliasName) {
        Assert.notNull(groupByProperty, "groupByProperty is required; it must not be null");
        Assert.notNull(groupByPropertyAliasName, "groupByPropertyAliasName is required; it must not be null");
        groupByAliasNames.put(groupByProperty, groupByPropertyAliasName);
        return this;
    }

    public String getGroupByProperty(String groupByProperty) {
        Assert.notNull(groupByProperty, "groupByProperty is required; it must not be null");
        return groupByAliasNames.get(groupByProperty);
    }

    public Map<String, String> getGroupByAliasNames() {
        return groupByAliasNames;
    }

    public void setGroupByAliasNames(Map<String, String> groupByAliasNames) {
        this.groupByAliasNames = groupByAliasNames;
    }

    public String getAggregateProperty() {
        return aggregateProperty;
    }

    public String getAggregateAliasName() {
        return aggregateAliasName;
    }

    public List<String> getGroupByProperties() {
        return groupByProperties;
    }

    public AggregateType getAggregateType() {
        return aggregateType;
    }
}
