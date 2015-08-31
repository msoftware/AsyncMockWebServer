package com.mosn.asyncmockwebserver;

import java.lang.reflect.Field;

public class ReflectionUtils {

    static Field getPrivateField(Class clazz, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
}
