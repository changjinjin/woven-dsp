package com.info.baymax.dsp.data.dataset.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class FieldMapping implements Comparable, Serializable {

    private static final long serialVersionUID = -8980720618228630927L;

    private int index;

    private String sourceField;

    private String sourceType;

    private String targetField;

    private String targetType;

    private String encrypt; // BLANK, MIX

    private TransformRule transformRule;

    public FieldMapping(String sourceField, String targetField) {
        this.sourceField = sourceField;
        this.targetField = targetField;
    }

    public FieldMapping(String sourceField, String sourceType, String targetField, String targetType) {
        this.sourceField = sourceField;
        this.sourceType = sourceType;
        this.targetField = targetField;
        this.targetType = targetType;
    }

    @Override
    public int compareTo(Object o) {
        FieldMapping another = (FieldMapping) o;
        return this.index > another.index ? 1 : -1;
    }
}
