package com.info.baymax.common.queryapi.query.sql;

import java.util.Arrays;
import java.util.Collection;

public interface SqlQueryBuilder<B extends SqlQueryBuilder<B>> {

    /**
     * 设置SQL查询模板
     *
     * @param sqlTemplate sql 模板
     * @return this builder
     */
    B sqlTemplate(String sqlTemplate);

    /**
     * 清理SQL查询模板
     *
     * @return this builder
     */
    B clearSqlTemplate();

    /**
     * 设置SQL查询参数列表
     *
     * @param sql 查询列表
     * @return this builder
     */
    B parameters(Collection<Parameter> parameters);

    /**
     * 添加参数数组
     *
     * @param parameters 参数数组
     * @return this builder
     */
    default B parameters(Parameter... parameters) {
        return parameters(Arrays.asList(parameters));
    }

    /**
     * 添加一个参数
     *
     * @param parameters 参数值
     * @return this builder
     */
    default B parameter(Parameter parameter) {
        return parameters(parameter);
    }

    /**
     * 清空parameters条件
     *
     * @return this builder
     */
    B clearParameters();

}
