package com.info.baymax.common.config.serialize.jackson.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Number数据格式化
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午3:46:14
 */
@Slf4j
public class NumberFormatDeserializer extends JsonDeserializer<Number> {

    /**
     * 格式
     */
    private String pattern;

    /**
     * 反序列化有效
     */
    private boolean deserialize;

    public NumberFormatDeserializer() {
    }

    public NumberFormatDeserializer(String pattern, boolean deserialize) {
        this.pattern = pattern;
        this.deserialize = deserialize;
    }

    @Override
    public Number deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = jp.getText();
        if (StringUtils.isNotEmpty(text) && StringUtils.isNotEmpty(pattern) && deserialize) {
            try {
                return new DecimalFormat(pattern).parse(text);
            } catch (ParseException e) {
                log.error(String.format("字段[%s]反序列化值[%s]不能匹配格式[%s]，反序列化失败", jp.getCurrentName(), text, pattern), e);
                throw new IOException(e);
            }
        }
        return jp.getNumberValue();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isDeserialize() {
        return deserialize;
    }

    public void setDeserialize(boolean deserialize) {
        this.deserialize = deserialize;
    }

}
