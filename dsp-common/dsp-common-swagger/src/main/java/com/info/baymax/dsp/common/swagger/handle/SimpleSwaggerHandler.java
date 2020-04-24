package com.info.baymax.dsp.common.swagger.handle;

import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.JsonSerializer;

/**
 * 简单的Swagger对象处理器
 *
 * @author jingwei.yang
 * @date 2020年4月23日 下午8:05:13
 */
@Component
@Slf4j
public class SimpleSwaggerHandler implements SwaggerHandler {

    @Autowired
    private JsonSerializer jsonSerializer;

    @Override
    public void handle(Swagger swagger) {
        if (swagger != null) {
            if (log.isDebugEnabled()) {
                log.debug(jsonSerializer.toJson(swagger).value());
            }
        }
    }
}
