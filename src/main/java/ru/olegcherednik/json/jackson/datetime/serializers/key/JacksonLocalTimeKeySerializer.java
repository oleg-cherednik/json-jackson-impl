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

package ru.olegcherednik.json.jackson.datetime.serializers.key;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 31.12.2023
 */
public class JacksonLocalTimeKeySerializer extends StdSerializer<LocalTime> {

    private static final long serialVersionUID = 290039851312525680L;

    protected final DateTimeFormatter df;

    public JacksonLocalTimeKeySerializer(DateTimeFormatter df) {
        super(LocalTime.class);
        this.df = df;
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String fieldName = useTimestamp(provider) ? getTimestampFieldName(value, gen, provider)
                                                  : getStringFieldName(value, gen, provider);

        gen.writeFieldName(fieldName);
    }

    protected String getStringFieldName(LocalTime value, JsonGenerator gen, SerializerProvider provider) {
        return df == null ? value.toString() : df.format(value);
    }

    protected String getTimestampFieldName(LocalTime value, JsonGenerator gen, SerializerProvider provider) {
        if (useNanoseconds(provider))
            return String.valueOf(value.toNanoOfDay());
        return String.valueOf(value.toSecondOfDay());
    }

    protected boolean useTimestamp(SerializerProvider provider) {
        return df == null && isEnabled(provider, SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
    }

    protected boolean useNanoseconds(SerializerProvider provider) {
        return isEnabled(provider, SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
    }

    protected static boolean isEnabled(SerializerProvider provider, SerializationFeature feature) {
        return provider.isEnabled(feature);
    }

}
