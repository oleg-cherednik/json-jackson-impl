/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.jackson.enumid.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.api.enumid.EnumIdSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Oleg Cherednik
 * @since 27.02.2022
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnumIdDeserializers extends SimpleDeserializers {

    private static final long serialVersionUID = -6984119149643932744L;

    public static final EnumIdDeserializers INSTANCE = new EnumIdDeserializers();

    private final Map<Class<?>, JsonDeserializer<?>> cache = new HashMap<>();

    @Override
    public synchronized JsonDeserializer<?> findEnumDeserializer(Class<?> type,
                                                                 DeserializationConfig config,
                                                                 BeanDescription beanDesc) throws JsonMappingException {
        return cache.computeIfAbsent(type, cls -> {
            Function<String, ?> read = EnumIdSupport.createFactory(type);

            return new JsonDeserializer<Object>() {
                @Override
                public Object deserialize(JsonParser in, DeserializationContext ctxt) throws IOException {
                    String id = in.getText();
                    return read.apply(id);
                }

                @Override
                public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                    return read.apply(null);
                }
            };
        });
    }

}
