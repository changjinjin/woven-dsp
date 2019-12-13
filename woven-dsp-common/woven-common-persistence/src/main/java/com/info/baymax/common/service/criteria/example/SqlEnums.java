package com.info.baymax.common.service.criteria.example;

public class SqlEnums {
    // 运算符
    public enum Operator {
        EQUAL, NOT_EQUAL, LIKE, NOT_LIKE, BETWEEN, NOT_BETWEEN, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, IS_NULL, NOT_NULL, IN, NOT_IN
    }

    // 逻辑关系
    public enum AndOr {
        AND, OR
    }

    // 排序类型
    public enum OrderType {
        ASC, DESC
    }
}