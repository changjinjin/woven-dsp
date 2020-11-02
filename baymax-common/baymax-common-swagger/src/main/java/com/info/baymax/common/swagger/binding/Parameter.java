package com.info.baymax.common.swagger.binding;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableRangeValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.VendorExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(doNotUseGetters = true)
@ToString
public class Parameter implements Ordered {
    private String name;
    private String description;
    private String defaultValue;
    private boolean required;
    private boolean allowMultiple;
    private String modelRef;
    private AllowableListValues allowableListValues;
    private AllowableRangeValues allowableRangeValues;
    private String paramType;
    private String paramAccess;
    private boolean hidden;
    private String pattern;
    private String collectionFormat;
    private int order;
    private Map<String, List<Example>> examples = new HashMap<String, List<Example>>();
    private boolean allowEmptyValue;

    public AllowableValues getAllowableValues() {
        return allowableListValues != null ? allowableListValues : allowableRangeValues;
    }

    public springfox.documentation.service.Parameter get() {
        return new springfox.documentation.service.Parameter(getName(), getDescription(), getDefaultValue(),
            isRequired(), isAllowMultiple(), isAllowEmptyValue(),
            new ModelRef(StringUtils.defaultIfEmpty(getModelRef(), "string")), ofNullable(null), getAllowableValues(),
            getParamType(), getParamAccess(), isHidden(), getPattern(), getCollectionFormat(), getOrder(), null,
            getExamples(), new ArrayList<VendorExtension>(0));
    }
}
