package com.info.baymax.common.swagger.handle;

import io.swagger.models.Swagger;

/**
 * Swagger对象处理器
 *
 * @author jingwei.yang
 * @date 2020年4月23日 下午8:05:13
 */
public interface SwaggerHandler {

    /**
     * swagger对象处理，比如可以把拿到的信息入库输出等
     *
     * @param swagger 系统初始化之后得到的swagger对象
     */
    void handle(Swagger swagger);

}
