package com.info.baymax.common.swagger.plugin;

import com.fasterxml.classmate.ResolvedType;
import com.info.baymax.common.swagger.annotation.ApiModelMap;
import com.info.baymax.common.swagger.utils.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.Map;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiModelMapReader implements ParameterBuilderPlugin {
    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        ResolvedType parameterType = methodParameter.getParameterType();
        if (Map.class.isAssignableFrom(parameterType.getErasedType())) { // 判断是否需要修改对象ModelRef,这里我判断的是Map类型和String类型需要重新修改ModelRef对象
            Optional<ApiModelMap> optional = methodParameter.findAnnotation(ApiModelMap.class); // 根据参数上的ApiJsonObject注解中的参数动态生成Class
            if (optional.isPresent()) {
                String modelClassName = genClassName(parameterContext.getOperationContext().requestMappingPattern(),
                    StringUtils.capitalize(methodParameter.defaultName().get()));
                parameterContext.parameterBuilder() // 修改Map参数的ModelRef为我们动态生成的class
                    .parameterType("body").modelRef(new ModelRef(modelClassName)).name(modelClassName);
            }
        }
    }

    public String genClassName(String rmp, String modelName) {
        rmp = StringUtils.removeStart(rmp, "/");
        rmp = StringUtils.removeEnd(rmp, "/");
        rmp = StringUtils.replaceChars(rmp, "{", "");
        rmp = StringUtils.replaceChars(rmp, "}", "");
        rmp = StringUtils.replaceChars(rmp, "/", "_");
        rmp = StringUtils.joinWith("_", rmp, modelName);
        return StringUtils.capitalize(StringUtils.toCamelCase(rmp));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
