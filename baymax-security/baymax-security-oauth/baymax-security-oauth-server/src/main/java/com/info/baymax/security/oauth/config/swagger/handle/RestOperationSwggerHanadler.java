package com.info.baymax.security.oauth.config.swagger.handle;

import com.google.common.collect.Lists;
import com.info.baymax.dsp.common.swagger.handle.SwaggerHandler;
import com.info.baymax.dsp.data.sys.entity.security.RestOperation;
import com.info.baymax.dsp.data.sys.service.security.RestOperationService;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;
import java.util.Map;

/**
 * 自定义Swagger文档处理器
 *
 * @author jingwei.yang
 * @date 2020年4月24日 上午9:52:34
 */
@Slf4j
@Component
public class RestOperationSwggerHanadler implements SwaggerHandler {

    @Autowired
    private RestOperationService restOperationService;

    @Value("${spring.application.name}")
    private String serviceName = "default";

    @Override
    public void handle(Swagger swagger) {
        if (swagger == null) {
            log.warn("swagger instance does not exist, swagger instance handle shiped!");
        }
        restOperationService.initRestOperations(serviceName, parse(serviceName, swagger));
    }

    private List<RestOperation> parse(String serviceName, Swagger swagger) {
        String basePath = swagger.getBasePath();
        Map<String, Path> paths = swagger.getPaths();
        final List<RestOperation> list = Lists.newArrayList();
        if (!paths.isEmpty()) {
            paths.forEach((p, path) -> {
                Map<HttpMethod, Operation> operationMap = path.getOperationMap();
                if (!operationMap.isEmpty()) {
                    operationMap.forEach((m, o) -> {
                        list.add(new RestOperation(serviceName, Docket.DEFAULT_GROUP_NAME, o.getTags(), m.name(),
                            basePath, p, o.getSummary(), o.getDescription(), o.getOperationId(), o.getConsumes(),
                            o.getProduces(), o.isDeprecated()));
                    });
                }
            });
        }
        return list;
    }
}
