package com.info.baymax.dsp.common.swagger.plugin;

import com.fasterxml.classmate.TypeResolver;
import com.info.baymax.dsp.common.swagger.annotation.ApiModelFields;
import com.info.baymax.dsp.common.swagger.utils.ClassUtils;
import io.swagger.annotations.ApiModelProperty;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Order
public class ApiModelFieldsReader implements ParameterBuilderPlugin {
    public static int CLASS_INDEX = 1;

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        Optional<ApiModelFields> apiModelFields = methodParameter.findAnnotation(ApiModelFields.class);
        if (apiModelFields.isPresent()) {
            Class<?> originClass = parameterContext.resolvedMethodParameter().getParameterType().getErasedType();
            String name = originClass.getSimpleName() + "LessModel_" + System.currentTimeMillis() + "_"
                + (CLASS_INDEX++); // model 名称
            try {
                parameterContext.getDocumentationContext().getAdditionalModels()
                    .add(new TypeResolver().resolve(createRefModelIgp(apiModelFields.get(), name, originClass))); // 像documentContext的Models中添加我们新生成的Class
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

            parameterContext.parameterBuilder() // 修改model参数的ModelRef为我们动态生成的class
                .parameterType("body").modelRef(new ModelRef(name)).name(name);
        }
    }

    /**
     * 根据propertys中的值动态生成含有Swagger注解的javaBean
     */
    private Class<?> createRefModelIgp(ApiModelFields ann, String name, Class<?> origin) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(origin.getPackage().getName() + "." + name);
        try {
            List<Field> fields = ClassUtils.getFields(origin, null, null);
            if (fields != null && !fields.isEmpty()) {
                Set<String> requiredFields = new HashSet<String>(Arrays.asList(ann.requiredFields()));
                Set<String> filterFields = new HashSet<String>(Arrays.asList(ann.filterFields()));
                boolean includeMode = ann.includeMode();

                List<Field> dealFileds = fields.stream()
                    .filter(f -> includeFields(requiredFields, filterFields, includeMode, f.getName()))//
                    .collect(Collectors.toList());
                createCtFileds(dealFileds, requiredFields, ctClass);
            }
            return ctClass.toClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean includeFields(Set<String> requiredFields, Set<String> filterFields, boolean includeMode,
                                 String fieldName) {
        if (includeMode) {
            return requiredFields.contains(fieldName) || filterFields.contains(fieldName);
        } else {
            return requiredFields.contains(fieldName) || !filterFields.contains(fieldName);
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    public void createCtFileds(List<Field> dealFileds, Set<String> requiredFields, CtClass ctClass)
        throws CannotCompileException, NotFoundException {
        for (Field field : dealFileds) {
            CtField ctField = new CtField(ClassPool.getDefault().get(field.getType().getName()), field.getName(),
                ctClass);
            ctField.setModifiers(Modifier.PUBLIC);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            String apiModelPropertyValue = java.util.Optional.ofNullable(annotation).map(s -> s.value()).orElse("");
            if (StringUtils.isNotBlank(apiModelPropertyValue)) { // 添加model属性说明
                ConstPool constPool = ctClass.getClassFile().getConstPool();
                AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                Annotation ann = new Annotation(ApiModelProperty.class.getName(), constPool);
                ann.addMemberValue("value", new StringMemberValue(apiModelPropertyValue, constPool));
                if (requiredFields.contains(field.getName())) {
                    ann.addMemberValue("required", new BooleanMemberValue(true, constPool));
                } else {
                    ann.addMemberValue("required", new BooleanMemberValue(false, constPool));
                }
                attr.addAnnotation(ann);
                ctField.getFieldInfo().addAttribute(attr);
            }
            ctClass.addField(ctField);
        }
    }
}