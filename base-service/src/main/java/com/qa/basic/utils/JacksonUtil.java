package com.qa.basic.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chasen on 2021/1/30.
 */
public class JacksonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public JacksonUtil() {
    }

    public static <T> T jsonToObj(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonParseException var3) {
            logger.error("JacksonUtils err {}", var3.getMessage(), var3);
        } catch (JsonMappingException var4) {
            logger.error("JacksonUtils err {}", var4.getMessage(), var4);
        } catch (IOException var5) {
            logger.error("JacksonUtils err {}", var5.getMessage(), var5);
        }

        return null;
    }

    public static <T> T jsonToObj(String json, TypeReference<?> reference) {
        try {
            return mapper.readValue(json, reference);
        } catch (IOException var3) {
            logger.error("JacksonUtils err {}", var3.getMessage(), var3);
            return null;
        }
    }

    public static String objToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException var2) {
            logger.error("JacksonUtils err {}", var2.getMessage(), var2);
            return null;
        }
    }

    public static String mapToJson(String key, String value) {
        HashMap map = new HashMap();
        map.put(key, value);

        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException var4) {
            logger.error("JacksonUtils err {}", var4.getMessage(), var4);
            return null;
        }
    }

    public static String mapToJson(Map<String, String> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException var2) {
            logger.error("JacksonUtils err {}", var2.getMessage(), var2);
            return null;
        }
    }

    public static <T> T mapToObjct(Map<String, String> map, Class<T> clazz) {
        try {
            return mapper.readValue(mapper.writeValueAsString(map), clazz);
        } catch (JsonProcessingException var3) {
            logger.error("JacksonUtils err {}", var3.getMessage(), var3);
        } catch (IOException var4) {
            logger.error("JacksonUtils err {}", var4.getMessage(), var4);
        }

        return null;
    }

    public static <T> T convertValue(Object map, TypeReference<T> typeReference) {
        return mapper.convertValue(map, typeReference);
    }

    static {
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
