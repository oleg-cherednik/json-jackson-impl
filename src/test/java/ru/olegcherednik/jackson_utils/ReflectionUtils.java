package ru.olegcherednik.jackson_utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minidev.asm.ex.NoSuchFieldException;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * @author Oleg Cherednik
 * @since 10.11.2023
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtils {

    public static <T> T getFieldValue(Object obj, String fieldName) {
        try {
            Field field = getField(obj.getClass(), fieldName);
            field.setAccessible(true);
            return (T)field.get(obj);
        } catch (Exception e) {
            throw new JacksonUtilsException(e);
        }
    }

    private static Field getField(Class<?> cls, String fieldName) {
        Field field = null;
        Class<?> clazz = cls;

        while (field == null && clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (java.lang.NoSuchFieldException ignored) {
                clazz = clazz.getSuperclass();
            }
        }

        return Optional.ofNullable(field).orElseThrow(NoSuchFieldException::new);
    }
}
