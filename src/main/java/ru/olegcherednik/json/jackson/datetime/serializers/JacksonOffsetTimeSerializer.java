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
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetTimeSerializer;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
public class JacksonOffsetTimeSerializer extends OffsetTimeSerializer {

    private static final long serialVersionUID = 458316630073810676L;

    public static final JacksonOffsetTimeSerializer INSTANCE = new JacksonOffsetTimeSerializer();

    protected final JsonFormat.Shape shape;

    public static JacksonOffsetTimeSerializer with(DateTimeFormatter df) {
        return new JacksonOffsetTimeSerializer(INSTANCE,
                                               INSTANCE._useTimestamp,
                                               INSTANCE._useNanoseconds,
                                               df,
                                               INSTANCE.shape);
    }

    protected JacksonOffsetTimeSerializer() {
        shape = null;
    }

    protected JacksonOffsetTimeSerializer(JacksonOffsetTimeSerializer base,
                                          Boolean useTimestamp,
                                          Boolean useNanoseconds,
                                          DateTimeFormatter df,
                                          JsonFormat.Shape shape) {
        super(base, useTimestamp, useNanoseconds, df);
        this.shape = shape;
    }

    @Override
    protected JacksonOffsetTimeSerializer withFormat(Boolean useTimestamp,
                                                     DateTimeFormatter df,
                                                     JsonFormat.Shape shape) {
        return new JacksonOffsetTimeSerializer(this, useTimestamp, _useNanoseconds, df, shape);
    }

    @Override
    protected JacksonOffsetTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new JacksonOffsetTimeSerializer(this, _useTimestamp, writeNanoseconds, _formatter, shape);
    }

    @Override
    @SuppressWarnings("PMD.AvoidReassigningParameters")
    public void serialize(OffsetTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (useTimestamp(provider))
            super.serialize(value, gen, provider);
        else if (_formatter == null)
            gen.writeString(value.toString());
        else {
            if (_formatter.getZone() != null) {
                ZoneOffset offset = _formatter.getZone().getRules().getOffset(Instant.now());
                value = value.withOffsetSameInstant(offset);
            }

            gen.writeString(_formatter.format(value));
        }
    }

}
