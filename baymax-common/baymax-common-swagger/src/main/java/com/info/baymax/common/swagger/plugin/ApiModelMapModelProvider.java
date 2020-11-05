package com.info.baymax.common.swagger.plugin;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import com.info.baymax.common.swagger.annotation.ApiModelMap;
import com.info.baymax.common.swagger.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.*;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1001)
@Slf4j
public class ApiModelMapModelProvider implements OperationModelsProviderPlugin {
    @Autowired
    private TypeResolver typeResolver;

    @SuppressWarnings("unchecked")
    @Override
    public void apply(RequestMappingContext context) {
        List<ResolvedMethodParameter> parameterTypes = context.getParameters();
        if (parameterTypes != null && parameterTypes.size() > 0) {
            for (ResolvedMethodParameter parameterType : parameterTypes) {
                if (Map.class.isAssignableFrom(parameterType.getParameterType().getErasedType())) {
                    Optional<ApiModelMap> optional = parameterType.findAnnotation(ApiModelMap.class);
                    if (optional.isPresent()) {
                        Set<String> patterns = context.getPatternsCondition().getPatterns();
                        String modelClassName = genClassName(
                            StringUtils.defaultIfBlank(context.getRequestMappingPattern(),
                                Lists.newArrayList(patterns).get(0)),
                            StringUtils.capitalize(parameterType.defaultName().get()));
                        Class<?> modelClass = createRefModel(optional.get(), modelClassName);
                        if (modelClass != null) {
                            // context.getDocumentationContext().getAdditionalModels()
                            // .add(typeResolver.resolve(modelClass));
                            context.operationModelsBuilder().addInputParam(typeResolver.resolve(modelClass));
                        }
                    }
                }
            }
        }
    }

    private Class<?> createRefModel(ApiModelMap apiModelMap, String className) {
        ApiModelProperty[] properties = apiModelMap.value();
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(className);
        ConstPool constpool = ctClass.getClassFile().getConstPool();

        // 添加ApiModel注解
        AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        attr.addAnnotation(new Annotation(ApiModel.class.getName(), constpool));
        ctClass.getClassFile().addAttribute(attr);
        try {
            for (ApiModelProperty property : properties) {
                ctClass.addField(createField(property, ctClass, constpool));
            }
            return ctClass.toClass();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private CtField createField(ApiModelProperty property, CtClass ctClass, ConstPool constPool)
        throws NotFoundException, CannotCompileException {
        CtField ctField = new CtField(getFieldType(property.dataType()), property.name(), ctClass);
        ctField.setModifiers(Modifier.PRIVATE);
        ctField.getFieldInfo().addAttribute(annotationsAttribute(ctField, constPool, property));
        return ctField;
    }

    @SuppressWarnings("deprecation")
    private AnnotationsAttribute annotationsAttribute(CtField ctField, ConstPool constPool, ApiModelProperty property) {
        Annotation ann = new Annotation(ApiModelProperty.class.getName(), constPool);
        ann.addMemberValue("value", new StringMemberValue(property.value(), constPool));
        ann.addMemberValue("name", new StringMemberValue(property.name(), constPool));
        ann.addMemberValue("allowableValues", new StringMemberValue(property.allowableValues(), constPool));
        ann.addMemberValue("access", new StringMemberValue(property.access(), constPool));
        ann.addMemberValue("notes", new StringMemberValue(property.notes(), constPool));
        ann.addMemberValue("dataType", new StringMemberValue(property.dataType(), constPool));
        ann.addMemberValue("required", new BooleanMemberValue(property.required(), constPool));
        // ann.addMemberValue("position", new IntegerMemberValue(property.position(), constPool));
        ann.addMemberValue("hidden", new BooleanMemberValue(property.hidden(), constPool));
        ann.addMemberValue("example", new StringMemberValue(property.example(), constPool));
        ann.addMemberValue("readOnly", new BooleanMemberValue(property.readOnly(), constPool));
        ann.addMemberValue("reference", new StringMemberValue(property.reference(), constPool));
        ann.addMemberValue("allowEmptyValue", new BooleanMemberValue(property.allowEmptyValue(), constPool));

        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        attr.addAnnotation(ann);
        return attr;
    }

    private CtClass getFieldType(String dataType) throws NotFoundException {
        Class<?> clazz = null;
        switch (dataType) {
            case "bool":
            case "boolean":
                clazz = Boolean.class;
                break;
            case "byte":
            case "short":
            case "int":
            case "int32":
            case "integer":
                clazz = Integer.class;
                break;
            case "int64":
            case "long":
                clazz = Long.class;
                break;
            case "float":
            case "double":
                clazz = Double.class;
                break;
            case "date-time":
            case "date":
            case "time":
            case "timestamp":
                clazz = Date.class;
                break;
            case "string":
            default:
                clazz = String.class;
                break;
        }
        return ClassPool.getDefault().get(clazz.getName());
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
