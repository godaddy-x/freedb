package com.pithy.free.spring.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static String toJSONString(Object object) {
        try {
            if (object == null) {
                return null;
            }
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败", e);
        }
        return null;
    }

    public static <T> T parseObject(String json, Class<T> object) {
        try {
            if (json == null || json.length() == 0) {
                return null;
            }
            return objectMapper.readValue(json, object);
        } catch (JsonProcessingException e) {
            log.error("JSON反序列化失败", e);
        }
        return null;
    }

    public static <T> List<T> parseArray(String json, Class<T> object) {
        try {
            return objectMapper.readValue(json, getCollectionType(objectMapper, List.class, object));
        } catch (JsonProcessingException e) {
            log.error("JSON反序列化失败", e);
        }
        return null;
    }

}