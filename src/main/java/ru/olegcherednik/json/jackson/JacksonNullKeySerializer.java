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

package ru.olegcherednik.json.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @author Oleg Cherednik
 * @since 02.01.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JacksonNullKeySerializer extends JsonSerializer<Object> {

    public static final String NULL_VALUE = "";


    public static final JacksonNullKeySerializer INSTANCE = new JacksonNullKeySerializer();

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(NULL_VALUE);
    }
}