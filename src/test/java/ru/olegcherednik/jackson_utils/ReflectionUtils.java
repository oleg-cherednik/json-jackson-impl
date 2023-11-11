/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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

    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    public static <T> T getFieldValue(Object obj, String fieldName) {
        try {
            Field field = getField(obj.getClass(), fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
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
