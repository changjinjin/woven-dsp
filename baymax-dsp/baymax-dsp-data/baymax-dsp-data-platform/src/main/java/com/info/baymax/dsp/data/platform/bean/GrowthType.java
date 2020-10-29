package com.info.baymax.dsp.data.platform.bean;

import com.info.baymax.common.enums.valuable.IntegerValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum GrowthType implements IntegerValuable {
    TOTAL(0, "全量"), //
    INCREMENT(1, "增量"), //
    LIST(2, "列表"); //

    private final Integer value;
    private final String label;

    public static Integer[] allowValues() {
        return Arrays.stream(values()).map(t -> t.getValue()).toArray(Integer[]::new);
    }

    public static GrowthType valueOf(Integer value) {
        for (GrowthType type : values()) {
            if (type.equalsTo(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("Wrong value %s for Enum type %s, allow values: %s.", value,
            GrowthType.class.getName(), Arrays.toString(allowValues())));
    }
}