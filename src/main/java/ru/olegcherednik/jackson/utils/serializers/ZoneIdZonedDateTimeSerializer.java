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
package ru.olegcherednik.jackson.utils.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import ru.olegcherednik.jackson.utils.JacksonObjectMapperBuilder;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 01.01.2021
 */
public class ZoneIdZonedDateTimeSerializer extends ZonedDateTimeSerializer {

    private static final long serialVersionUID = -2135138754031293296L;

    private final UnaryOperator<ZoneId> zoneModifier;

    public ZoneIdZonedDateTimeSerializer(UnaryOperator<ZoneId> zoneModifier) {
        super(new ZonedDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                null,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                false);
        this.zoneModifier = Optional.ofNullable(zoneModifier).orElse(JacksonObjectMapperBuilder.ZONE_MODIFIER_USE_ORIGINAL);
    }

    protected ZoneIdZonedDateTimeSerializer(ZoneIdZonedDateTimeSerializer base, Boolean useTimestamp, DateTimeFormatter formatter,
            Boolean writeZoneId) {
        this(base, useTimestamp, null, formatter, writeZoneId);
    }

    protected ZoneIdZonedDateTimeSerializer(ZoneIdZonedDateTimeSerializer base, Boolean useTimestamp, Boolean useNanoseconds,
            DateTimeFormatter formatter, Boolean writeZoneId) {
        super(base, useTimestamp, useNanoseconds, formatter, writeZoneId);
        zoneModifier = base.zoneModifier;
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
        ZoneId zone = zoneModifier.apply(value.getZone());
        super.serialize(value.withZoneSameInstant(zone), g, provider);
    }

    @Override
    protected ZoneIdZonedDateTimeSerializer withFormat(Boolean useTimestamp, DateTimeFormatter formatter, JsonFormat.Shape shape) {
        return new ZoneIdZonedDateTimeSerializer(this, useTimestamp, formatter, false);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected ZoneIdZonedDateTimeSerializer withFeatures(Boolean writeZoneId) {
        return new ZoneIdZonedDateTimeSerializer(this, _useTimestamp, _formatter, writeZoneId);
    }

    @Override
    protected ZoneIdZonedDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new ZoneIdZonedDateTimeSerializer(this, _useTimestamp, writeNanoseconds, _formatter, writeZoneId);
    }

}
