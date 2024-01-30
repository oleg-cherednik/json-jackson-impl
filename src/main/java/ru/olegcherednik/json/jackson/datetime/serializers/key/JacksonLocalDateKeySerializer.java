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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 31.12.2023
 */
public class JacksonLocalDateKeySerializer extends StdSerializer<LocalDate> {

    private static final long serialVersionUID = 9115904762296059864L;

    protected final DateTimeFormatter df;

    public JacksonLocalDateKeySerializer(DateTimeFormatter df) {
        super(LocalDate.class);
        this.df = df;
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String fieldName = useTimestamp(provider) ? getTimestampFieldName(value, gen, provider)
                                                  : getStringFieldName(value, gen, provider);

        gen.writeFieldName(fieldName);
    }

    protected String getTimestampFieldName(LocalDate value, JsonGenerator gen, SerializerProvider provider) {
        return String.valueOf(value.toEpochDay());
    }

    protected String getStringFieldName(LocalDate value, JsonGenerator gen, SerializerProvider provider) {
        return df == null ? value.toString() : df.format(value);
    }

    protected boolean useTimestamp(SerializerProvider provider) {
        return df == null && isEnabled(provider, SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
    }

    protected static boolean isEnabled(SerializerProvider provider, SerializationFeature feature) {
        return provider.isEnabled(feature);
    }

}