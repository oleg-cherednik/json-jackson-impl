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
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetTimeSerializer;
import ru.olegcherednik.json.api.JsonSettings;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
public class JacksonOffsetTimeSerializer extends OffsetTimeSerializer {

    private static final long serialVersionUID = 458316630073810676L;

    public static final JacksonOffsetTimeSerializer INSTANCE = new JacksonOffsetTimeSerializer();

    protected final UnaryOperator<ZoneId> zoneModifier;

    protected JacksonOffsetTimeSerializer() {
        zoneModifier = JsonSettings.DEFAULT_ZONE_MODIFIER;
    }

    protected JacksonOffsetTimeSerializer(JacksonOffsetTimeSerializer base,
                                          Boolean useTimestamp,
                                          Boolean useNanoseconds,
                                          DateTimeFormatter df,
                                          UnaryOperator<ZoneId> zoneModifier) {
        super(base, useTimestamp, useNanoseconds, df);
        this.zoneModifier = zoneModifier;
    }

    @Override
    protected JacksonOffsetTimeSerializer withFormat(Boolean useTimestamp, DateTimeFormatter df, JsonFormat.Shape shape) {
        return new JacksonOffsetTimeSerializer(this, useTimestamp, _useNanoseconds, df, zoneModifier);
    }

    @Override
    protected JacksonOffsetTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new JacksonOffsetTimeSerializer(this, _useTimestamp, writeNanoseconds, _formatter, zoneModifier);
    }

    public JacksonOffsetTimeSerializer with(DateTimeFormatter df, UnaryOperator<ZoneId> zoneModifier) {
        return new JacksonOffsetTimeSerializer(this, _useTimestamp, _useNanoseconds, df, zoneModifier);
    }

    @Override
    @SuppressWarnings("PMD.AvoidReassigningParameters")
    public void serialize(OffsetTime value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (_formatter == null || useTimestamp(provider))
            super.serialize(value, generator, provider);
        else {
            ZoneId zoneId = _formatter.getZone() == null ? zoneModifier.apply(value.getOffset())
                                                         : _formatter.getZone();
            ZoneOffset offset = zoneId.getRules().getOffset(Instant.now());
            value = value.withOffsetSameInstant(offset);
            generator.writeString(_formatter.withZone(zoneId).format(value));
        }
    }

}
