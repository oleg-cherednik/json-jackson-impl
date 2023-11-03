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

import java.util.Optional;

/**
 * This interface hides enum implementation and enum constant id. In general case, it is not good to use
 * {@link Enum#name()} as constant in database or in any formatted string, because after any refactoring,
 * all existed database records should be modified as well.<p>
 * For serialization, the enum should override {@link #getId()} to define a string value.<p>
 * For deserialization, the enum should contain exactly one static method with single {@link String} argument
 * and either be annotated with {@link com.fasterxml.jackson.annotation.JsonCreator} annotation or has name <b>parseId</b>.
 *
 * @author Oleg Cherednik
 * @since 17.10.2021
 */
public interface EnumId {

    String name();

    default String getId() {
        return name();
    }

    static <T extends Enum<?> & EnumId> String getId(T obj, T def) {
        return Optional.ofNullable(obj).orElse(def).getId();
    }

    static <T extends Enum<?> & EnumId> T parseName(Class<T> cls, String name) {
        T res = parseName(cls.getEnumConstants(), name, null);

        if (res != null)
            return res;

        throw new EnumConstantNotPresentException(cls, name);
    }

    static <T extends Enum<?> & EnumId> T parseName(Class<T> cls, String name, T def) {
        return parseName(cls.getEnumConstants(), name, def);
    }

    static <T extends EnumId> T parseName(T[] values, String name, T def) {
        for (T value : values)
            if (value.name().equalsIgnoreCase(name))
                return value;

        return def;
    }

    static <T extends Enum<?> & EnumId> T parseIdOrName(Class<T> cls, String idOrName) {
        T res = parseId(cls.getEnumConstants(), idOrName, null);
        return Optional.ofNullable(res).orElseGet(() -> parseName(cls, idOrName));
    }

    static <T extends Enum<?> & EnumId> T parseId(Class<T> cls, String id) {
        T res = parseId(cls.getEnumConstants(), id, null);
        return Optional.ofNullable(res).orElseThrow(() -> new EnumConstantNotPresentException(cls, id));
    }

    static <T extends Enum<?> & EnumId> T parseId(Class<T> cls, String id, T def) {
        return parseId(cls.getEnumConstants(), id, def);
    }

    static <T extends EnumId> T parseId(T[] values, String id, T def) {
        for (T value : values)
            if (id == null ? value.getId() == null : id.equalsIgnoreCase(value.getId()))
                return value;

        return def;
    }

}
