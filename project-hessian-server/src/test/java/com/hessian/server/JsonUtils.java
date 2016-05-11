package com.hessian.server;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private final static ObjectMapper jacksonMapper = new ObjectMapper();

    public static <T> T fromJson(Class<T> clazz, String jsonStr) {
        T jsonObj = null;
        if (jsonStr != null && !"".equals(jsonStr)) {
            try {
                jsonObj = jacksonMapper.readValue(jsonStr, clazz);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return jsonObj;
    }

    public static String toJson(Object obj) {
        if (obj != null) {
            try {
                return jacksonMapper.writeValueAsString(obj);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }

}
