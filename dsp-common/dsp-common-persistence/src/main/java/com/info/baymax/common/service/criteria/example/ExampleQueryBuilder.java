package com.info.baymax.common.service.criteria.example;

/**
 * 查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface ExampleQueryBuilder<B extends ExampleQueryBuilder<B>> {


    /**
     * 指定计数字段，是有统计技术的查询才有用，默认不需指定
     *
     * @param countProperty 计数字段
     * @return this builder
     */
    B countProperty(String countProperty);

    /**
     * 设置join sql
     *
     * @param joinSql join sql
     * @return this builder
     */
    B joinSql(JoinSql joinSql);


    /**
     * 清空CountProperty条件
     *
     * @return this builder
     */
    B clearCountProperty();

    /**
     * 清空JoinSql条件
     *
     * @return this builder
     */
    B clearJoinSql();
}
