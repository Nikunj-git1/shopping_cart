package com.example.shopping_cart.util;

import java.lang.reflect.Field;

public class PatchMapper {

    public static <T> void applyNonNullFields(T source, T target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source और Target null नहीं हो सकते");
        }

        Class<?> clazz = source.getClass();

        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(source);
                    if (value != null) {
                        field.set(target, value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("फील्ड सेट करते समय Error आया: " + field.getName(), e);
                }
            }

            clazz = clazz.getSuperclass(); // सपोर्ट करता है inheritance को भी
        }
    }
}

// How to Call in ServiceImpl
//PatchMapper.applyNonNullFields(update, existing);
//
//    adminRepository.save(existing);