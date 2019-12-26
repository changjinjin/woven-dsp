package com.info.baymax.common.comp.swagger.plugin;

import com.fasterxml.classmate.TypeResolver;
import com.info.baymax.common.comp.swagger.annotation.ApiModelMap;
import io.swagger.annotations.ApiModelProperty;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
@Order // plugin加载顺序，默认是最后加载
public class ApiModelMapReader implements ParameterBuilderPlugin {

    public static int CLASS_INDEX = 1;

    @Autowired
    private TypeResolver typeResolver;

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();

        if (methodParameter.getParameterType().canCreateSubtype(Map.class)
            || methodParameter.getParameterType().canCreateSubtype(String.class)) { // 判断是否需要修改对象ModelRef,这里我判断的是Map类型和String类型需要重新修改ModelRef对象
            Optional<ApiModelMap> optional = methodParameter.findAnnotation(ApiModelMap.class); // 根据参数上的ApiJsonObject注解中的参数动态生成Class
            if (optional.isPresent()) {
                ApiModelProperty[] properties = optional.get().value();
                String name = "MapClass_" + System.currentTimeMillis() + "_" + (CLASS_INDEX++);
                parameterContext.getDocumentationContext().getAdditionalModels()
                    .add(typeResolver.resolve(createRefModel(properties, name))); // 像documentContext的Models中添加我们新生成的Class

                parameterContext.parameterBuilder() // 修改Map参数的ModelRef为我们动态生成的class
                    .parameterType("body").modelRef(new ModelRef(name)).name(name);
            }
        }

    }

    private final static String basePackage = "com.xx.xxx.in.swagger.model."; // 动态生成的Class名

    /**
     * 根据propertys中的值动态生成含有Swagger注解的javaBeen
     */
    private Class<?> createRefModel(ApiModelProperty[] propertys, String name) {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(basePackage + name);
        try {
            for (ApiModelProperty property : propertys) {
                ctClass.addField(createField(property, ctClass));
            }
            return ctClass.toClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据property的值生成含有swagger apiModelProperty注解的属性
     */
    private CtField createField(ApiModelProperty property, CtClass ctClass)
        throws NotFoundException, CannotCompileException {
        CtField ctField = new CtField(getFieldType(property.dataType()), property.name(), ctClass);
        ctField.setModifiers(Modifier.PUBLIC);
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        ctField.getFieldInfo().addAttribute(annotationsAttribute(ctField, constPool, property));
        return ctField;
    }

    @SuppressWarnings("deprecation")
    private AnnotationsAttribute annotationsAttribute(CtField ctField, ConstPool constPool, ApiModelProperty property) {
        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation ann = new Annotation("io.swagger.annotations.ApiModelProperty", constPool);
        ann.addMemberValue("value", new StringMemberValue(property.value(), constPool));
        ann.addMemberValue("name", new StringMemberValue(property.name(), constPool));
        ann.addMemberValue("allowableValues", new StringMemberValue(property.allowableValues(), constPool));
        ann.addMemberValue("access", new StringMemberValue(property.access(), constPool));
        ann.addMemberValue("notes", new StringMemberValue(property.notes(), constPool));
        ann.addMemberValue("dataType", new StringMemberValue(property.dataType(), constPool));
        ann.addMemberValue("required", new BooleanMemberValue(property.required(), constPool));
        ann.addMemberValue("position", new IntegerMemberValue(property.position(), constPool));
        ann.addMemberValue("hidden", new BooleanMemberValue(property.hidden(), constPool));
        ann.addMemberValue("example", new StringMemberValue(property.example(), constPool));
        ann.addMemberValue("readOnly", new BooleanMemberValue(property.readOnly(), constPool));
        ann.addMemberValue("reference", new StringMemberValue(property.reference(), constPool));
        ann.addMemberValue("allowEmptyValue", new BooleanMemberValue(property.allowEmptyValue(), constPool));
        return attr;
    }

    private CtClass getFieldType(String type) throws NotFoundException {
        CtClass fileType = null;
        switch (type) {
            case "string":
                fileType = ClassPool.getDefault().get(String.class.getName());
                break;
            case "int":
            case "integer":
            case "int32":
            case "int64":
            case "long":
                fileType = ClassPool.getDefault().get(Integer.class.getName());
                break;
            case "float":
            case "double":
                fileType = ClassPool.getDefault().get(Double.class.getName());
                break;
            case "date-time":
            case "date":
            case "time":
                fileType = ClassPool.getDefault().get(Date.class.getName());
                break;
        }
        return fileType;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
