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

package ru.olegcherednik.json.jackson.utils.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JacksonLocalTimeSerializer extends LocalTimeSerializer {

    private static final long serialVersionUID = 8966284219834243016L;

    public static final JacksonLocalTimeSerializer INSTANCE = new JacksonLocalTimeSerializer();

    private static final ZoneId SYSTEM_DEFAULT_ZONE_ID = ZoneId.systemDefault();

    protected JacksonLocalTimeSerializer(JacksonLocalTimeSerializer base,
                                         Boolean useTimestamp,
                                         Boolean useNanoseconds,
                                         DateTimeFormatter df) {
        super(base, useTimestamp, useNanoseconds, df);
    }

    @Override
    protected JacksonLocalTimeSerializer withFormat(Boolean useTimestamp,
                                                    DateTimeFormatter df,
                                                    JsonFormat.Shape shape) {
        return new JacksonLocalTimeSerializer(this, useTimestamp, _useNanoseconds, df);
    }

    public JacksonLocalTimeSerializer with(DateTimeFormatter df) {
        return new JacksonLocalTimeSerializer(this, _useTimestamp, _useNanoseconds, df);
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        if (_formatter == null || useTimestamp(provider))
            super.serialize(value, generator, provider);
        else
            generator.writeString(_formatter.withZone(SYSTEM_DEFAULT_ZONE_ID).format(value));
    }
}
