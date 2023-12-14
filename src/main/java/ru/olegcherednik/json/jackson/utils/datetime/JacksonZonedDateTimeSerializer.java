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
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import ru.olegcherednik.json.api.JsonSettings;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
public class JacksonZonedDateTimeSerializer extends ZonedDateTimeSerializer {

    private static final long serialVersionUID = -6441103051278765460L;

    public static final JacksonZonedDateTimeSerializer INSTANCE = new JacksonZonedDateTimeSerializer();

    protected final UnaryOperator<ZoneId> zoneModifier;

    protected JacksonZonedDateTimeSerializer() {
        zoneModifier = JsonSettings.DEFAULT_ZONE_MODIFIER;
    }

    protected JacksonZonedDateTimeSerializer(JacksonZonedDateTimeSerializer base,
                                             Boolean useTimestamp,
                                             Boolean useNanoseconds,
                                             DateTimeFormatter df,
                                             Boolean writeZoneId,
                                             UnaryOperator<ZoneId> zoneModifier) {
        super(base, useTimestamp, useNanoseconds, df, writeZoneId);
        this.zoneModifier = zoneModifier;
    }

    public JacksonZonedDateTimeSerializer with(DateTimeFormatter df, UnaryOperator<ZoneId> zoneModifier) {
        return new JacksonZonedDateTimeSerializer(this, _useTimestamp, _useNanoseconds, df,
                                                  _writeZoneId != null && _writeZoneId, zoneModifier);
    }

    @Override
    protected JacksonZonedDateTimeSerializer withFormat(Boolean useTimestamp, DateTimeFormatter df, JsonFormat.Shape shape) {
        return new JacksonZonedDateTimeSerializer(this, useTimestamp, _useNanoseconds,
                                                  df, _writeZoneId, zoneModifier);
    }

    @Override
    protected JacksonZonedDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new JacksonZonedDateTimeSerializer(this, _useTimestamp, writeNanoseconds,
                                                  _formatter, writeZoneId, zoneModifier);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        if (_formatter == null || useTimestamp(provider))
            super.serialize(value, generator, provider);
        else if (_formatter.getZone() == null) {
            ZoneId zoneId = zoneModifier.apply(DateTimeFormatter.ISO_OFFSET_DATE_TIME.getZone());
            generator.writeString(_formatter.withZone(zoneId).format(value));
        } else
            generator.writeString(_formatter.format(value));
    }

}
