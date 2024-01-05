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
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 31.12.2023
 */
public class JacksonLocalDateTimeKeySerializer extends StdSerializer<LocalDateTime> {

    private static final long serialVersionUID = 8090551111500462382L;

    protected final DateTimeFormatter df;

    public JacksonLocalDateTimeKeySerializer(DateTimeFormatter df) {
        super(LocalDateTime.class);
        this.df = df;
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String fieldName = getStringFieldName(value, gen, provider);
        gen.writeFieldName(fieldName);
    }

    protected String getStringFieldName(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) {
        return df == null ? value.toString() : df.format(value);
    }

}
