package com.info.baymax.common.config.serialize.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Number数据格式化
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午3:46:29
 */
@Slf4j
public class NumberFormatSerializer extends JsonSerializer<Number> {
    private final String DF_PATTERN = "0.##";

    /**
     * 格式
     */
    private String pattern;

    /**
     * 序列化有效
     */
    private boolean serialize;

    public NumberFormatSerializer() {
    }

    public NumberFormatSerializer(String pattern, boolean serialize) {
        this.pattern = pattern;
        this.serialize = serialize;
    }

    @Override
    public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
        if (StringUtils.isEmpty(pattern) || !serialize) {
            pattern = DF_PATTERN;
        }
        try {
            if (value != null) {
                jgen.writeString(new DecimalFormat(pattern).format(value));
            }
        } catch (Exception e) {
            log.error(String.format("序列化值[%s]不能匹配格式[%s]，序列化失败", value, pattern), e);
            throw new IOException(e);
        }
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isSerialize() {
        return serialize;
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }

}
