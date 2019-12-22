package com.info.baymax.dsp.data.platform.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: haijun
 * @Date: 2019/12/20 19:42
 */
@Data
public class TransformRule implements Serializable {
    private static final long serialVersionUID = 6483944460769157873L;
    /**desensitization, encryption, Transformation
     * support: blank, replace, md5, base64, udf
    */
    private String type;
    private String expression;//base(col) as field

    public static final String RULE_BLANK = "";//'' as field_name
    public static final String RULE_REPLACE = "";//''
//    public static final String RULE_MD5 = "";
    public static final String RULE_BASE64 = "";//base64(field) as new_field
    public static final String RULE_UDF = "";//test_udf() as new_field
}
