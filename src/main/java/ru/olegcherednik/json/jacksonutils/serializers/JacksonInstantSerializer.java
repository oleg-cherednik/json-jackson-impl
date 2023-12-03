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

package ru.olegcherednik.json.jacksonutils.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializerBase;
import ru.olegcherednik.json.api.JsonSettings;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE;

/**
 * @author Oleg Cherednik
 * @since 27.11.2023
 */
public class JacksonInstantSerializer extends InstantSerializerBase<Instant> {

    private static final long serialVersionUID = -4270313943618715425L;

    public static final JacksonInstantSerializer INSTANCE = new JacksonInstantSerializer();

    private final UnaryOperator<ZoneId> zoneModifier;
    private final DateTimeFormatter defaultFormat;

    protected JacksonInstantSerializer() {
        super(Instant.class, Instant::toEpochMilli, Instant::getEpochSecond,
              Instant::getNano, getDefaultFormatter());
        zoneModifier = JsonSettings.DEFAULT_ZONE_MODIFIER;
        defaultFormat = getDefaultFormatter();
    }

    private static DateTimeFormatter getDefaultFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
                                .withZone(ZoneId.systemDefault());
    }

    protected JacksonInstantSerializer(JacksonInstantSerializer base,
                                       Boolean useTimestamp,
                                       DateTimeFormatter df,
                                       JsonFormat.Shape shape,
                                       UnaryOperator<ZoneId> zoneModifier) {
        super(base, useTimestamp, base._useNanoseconds, df, shape);
        this.zoneModifier = zoneModifier;
        defaultFormat = base.defaultFormat;
    }

    @Override
    protected JacksonInstantSerializer withFormat(Boolean useTimestamp, DateTimeFormatter df, JsonFormat.Shape shape) {
        return new JacksonInstantSerializer(this, useTimestamp, df, shape, zoneModifier);
    }

    public JacksonInstantSerializer with(DateTimeFormatter df, UnaryOperator<ZoneId> zoneModifier) {
        return new JacksonInstantSerializer(this, _useTimestamp, df, _shape, zoneModifier);
    }

    @Override
    public void serialize(Instant value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        if (_formatter == null)
            super.serialize(value, generator, provider);
        else {
            ZoneId zoneId = zoneModifier.apply(defaultFormat.getZone());
            generator.writeString(_formatter.withZone(zoneId).format(value));
        }
    }

    @Override
    protected String formatValue(Instant value, SerializerProvider provider) {
        assert _formatter == null;
        assert defaultFormat != null;

        ZoneId zoneId = zoneModifier.apply(defaultFormat.getZone());

        if (provider.getConfig().hasExplicitTimeZone() && provider.isEnabled(WRITE_DATES_WITH_CONTEXT_TIME_ZONE))
            zoneId = provider.getTimeZone().toZoneId();

        return defaultFormat.withZone(zoneId).format(value);
    }

}
