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

package ru.olegcherednik.json.jackson.datetime.serializers.key;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 31.12.2023
 */
public class JacksonOffsetTimeKeySerializer extends StdSerializer<OffsetTime> {

    private static final long serialVersionUID = 4055988949929446691L;

    protected final DateTimeFormatter df;

    public JacksonOffsetTimeKeySerializer(DateTimeFormatter df) {
        super(OffsetTime.class);
        this.df = df;
    }

    @Override
    public void serialize(OffsetTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String fieldName = getStringFieldName(value, gen, provider);
        gen.writeFieldName(fieldName);
    }

    protected String getStringFieldName(OffsetTime value, JsonGenerator gen, SerializerProvider provider) {
        if (df == null)
            return value.toString();

        if (df.getZone() != null) {
            ZoneOffset offset = df.getZone().getRules().getOffset(Instant.now());
            value = value.withOffsetSameInstant(offset);
        }

        return df.format(value);
    }

}