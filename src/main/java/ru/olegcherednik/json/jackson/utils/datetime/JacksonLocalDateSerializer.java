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

package ru.olegcherednik.json.jackson.utils.datetime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JacksonLocalDateSerializer extends LocalDateSerializer {

    private static final long serialVersionUID = 613205055855252252L;

    public static final JacksonLocalDateSerializer INSTANCE = new JacksonLocalDateSerializer();

    private static final ZoneId SYSTEM_DEFAULT_ZONE_ID = ZoneId.systemDefault();

    protected JacksonLocalDateSerializer(JacksonLocalDateSerializer base,
                                         Boolean useTimestamp,
                                         DateTimeFormatter df,
                                         JsonFormat.Shape shape) {
        super(base, useTimestamp, df, shape);
    }

    @Override
    protected JacksonLocalDateSerializer withFormat(Boolean useTimestamp,
                                                    DateTimeFormatter df,
                                                    JsonFormat.Shape shape) {
        return new JacksonLocalDateSerializer(this, useTimestamp, df, shape);
    }

    public JacksonLocalDateSerializer with(DateTimeFormatter df) {
        return new JacksonLocalDateSerializer(this, _useTimestamp, df, _shape);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        if (_formatter == null || useTimestamp(provider))
            super.serialize(value, generator, provider);
        else
            generator.writeString(_formatter.withZone(SYSTEM_DEFAULT_ZONE_ID).format(value));
    }

}
