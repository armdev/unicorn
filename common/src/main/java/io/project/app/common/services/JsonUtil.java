package io.project.app.common.services;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

    public static <T> T stringToBean(String strValue, Class<T> clazz) {

        if ((strValue == null) || (strValue.length() <= 0) || (clazz == null)) {
            return null;
        }

        // int or Integer
        if ((clazz == int.class) || (clazz == Integer.class)) {
            return (T) Integer.valueOf(strValue);
        } // long or Long
        else if ((clazz == long.class) || (clazz == Long.class)) {
            return (T) Long.valueOf(strValue);
        } // String
        else if (clazz == String.class) {
            return (T) strValue;
        } // 对象类型
        else {
            return JSON.toJavaObject(JSON.parseObject(strValue), clazz);
        }
    }

    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }

        Class<?> clazz = value.getClass();

        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }
}
