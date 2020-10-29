package com.info.baymax.dsp.data.dataset.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InputOutputField {

    private String column;
    private String alias;

    public InputOutputField() {
    }

    public InputOutputField(String column) {
        this.column = column;
    }

    public InputOutputField(String column, String alias) {
        this.column = column;
        this.alias = alias;
    }
}
