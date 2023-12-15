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
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import ru.olegcherednik.json.api.JsonSettings;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
public class JacksonOffsetDateTimeSerializer extends OffsetDateTimeSerializer {

    private static final long serialVersionUID = -954133191249891146L;

    public static final JacksonOffsetDateTimeSerializer INSTANCE = new JacksonOffsetDateTimeSerializer();

    protected final UnaryOperator<ZoneId> zoneModifier;

    public static JacksonOffsetDateTimeSerializer with(DateTimeFormatter df, UnaryOperator<ZoneId> zoneModifier) {
        return new JacksonOffsetDateTimeSerializer(INSTANCE,
                                                   INSTANCE._useTimestamp,
                                                   INSTANCE._useNanoseconds,
                                                   df,
                                                   zoneModifier);
    }

    protected JacksonOffsetDateTimeSerializer() {
        zoneModifier = JsonSettings.DEFAULT_ZONE_MODIFIER;
    }

    protected JacksonOffsetDateTimeSerializer(JacksonOffsetDateTimeSerializer base,
                                              Boolean useTimestamp,
                                              Boolean useNanoseconds,
                                              DateTimeFormatter df,
                                              UnaryOperator<ZoneId> zoneModifier) {
        super(base, useTimestamp, useNanoseconds, df);
        this.zoneModifier = zoneModifier;
    }

    @Override
    protected JacksonOffsetDateTimeSerializer withFormat(Boolean useTimestamp,
                                                         DateTimeFormatter df,
                                                         JsonFormat.Shape shape) {
        return new JacksonOffsetDateTimeSerializer(this, useTimestamp, _useNanoseconds, df, zoneModifier);
    }

    @Override
    protected JacksonOffsetDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean useNanoseconds) {
        return new JacksonOffsetDateTimeSerializer(this, _useTimestamp, useNanoseconds, _formatter, zoneModifier);
    }

    @Override
    public void serialize(OffsetDateTime value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        if (_formatter == null || useTimestamp(provider))
            super.serialize(value, generator, provider);
        else {
            ZoneId zoneId = _formatter.getZone() == null ? zoneModifier.apply(value.getOffset())
                                                         : _formatter.getZone();

            generator.writeString(_formatter.format(value.atZoneSameInstant(zoneId)));
        }
    }

}
