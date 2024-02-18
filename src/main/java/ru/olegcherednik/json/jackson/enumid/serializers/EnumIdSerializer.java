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

package ru.olegcherednik.json.jackson.enumid.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.api.enumid.EnumId;

import java.io.IOException;

/**
 * Custom serializer for all {@link EnumId}. It just invokes {@link EnumId#getId()}. Since that, standard {@link
 * com.fasterxml.jackson.databind.deser.std.EnumDeserializer} will not be working for these types, because by default
 * {@link EnumId#getId()} returns lower-case enum constant name (instead of upper-case by default). Therefor you have
 * to define {@link ru.olegcherednik.json.api.enumid.EnumIdJsonCreator} for every {@link EnumId} instance.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnumIdSerializer extends JsonSerializer<EnumId> {

    public static final EnumIdSerializer INSTANCE = new EnumIdSerializer();

    @Override
    public Class<EnumId> handledType() {
        return EnumId.class;
    }

    @Override
    public void serialize(EnumId enumId, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeString(enumId.getId());
    }

}
