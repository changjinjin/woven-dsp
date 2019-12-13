package com.info.baymax.common.service.criteria.example;

/**
 * LIKE 条件生成器
 *
 * @author jingwei.yang
 * @date 2019年9月6日 上午11:30:39
 */
public class LikeValueMaker {

    public String like(String value) {
        return "%".concat(value).concat("%");
    }

    public String leftLike(String value) {
        return value.concat("%");
    }

    public String rigthLike(String value) {
        return "%".concat(value);
    }

}
