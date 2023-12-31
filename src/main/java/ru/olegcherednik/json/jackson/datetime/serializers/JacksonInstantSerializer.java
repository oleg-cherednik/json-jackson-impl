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

package ru.olegcherednik.json.jackson.datetime.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializerBase;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 27.11.2023
 */
public class JacksonInstantSerializer extends InstantSerializerBase<Instant> {

    private static final long serialVersionUID = -3249627908753207289L;

    public static final JacksonInstantSerializer INSTANCE = new JacksonInstantSerializer();

    protected JacksonInstantSerializer() {
        super(Instant.class, Instant::toEpochMilli, Instant::getEpochSecond, Instant::getNano, null);
    }

    public JacksonInstantSerializer(DateTimeFormatter df) {
        super(INSTANCE, INSTANCE._useTimestamp, INSTANCE._useNanoseconds, df);
    }

    protected JacksonInstantSerializer(JacksonInstantSerializer base,
                                       Boolean useTimestamp,
                                       DateTimeFormatter df,
                                       JsonFormat.Shape shape) {
        super(base, useTimestamp, base._useNanoseconds, df, shape);
    }

    protected JacksonInstantSerializer(JacksonInstantSerializer base,
                                       Boolean useTimestamp,
                                       Boolean useNanoseconds,
                                       DateTimeFormatter df) {
        super(base, useTimestamp, useNanoseconds, df);
    }

    @Override
    protected JacksonInstantSerializer withFormat(Boolean useTimestamp, DateTimeFormatter df, JsonFormat.Shape shape) {
        return new JacksonInstantSerializer(this, useTimestamp, df, shape);
    }

    @Override
    protected JacksonInstantSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new JacksonInstantSerializer(this, _useTimestamp, writeNanoseconds, _formatter);
    }

    @Override
    protected String formatValue(Instant value, SerializerProvider provider) {
        return super.formatValue(value, provider);
    }

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        super.serialize(value, gen, provider);
    }
}
