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
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import ru.olegcherednik.jackson.utils.JacksonObjectMapperSupplier;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 01.01.2021
 */
public class JacksonUtilsZonedDateTimeSerializer extends ZonedDateTimeSerializer {

    private static final long serialVersionUID = -2135138754031293296L;

    private final UnaryOperator<ZoneId> zoneModifier;
    private final boolean useMilliseconds;

    public JacksonUtilsZonedDateTimeSerializer(UnaryOperator<ZoneId> zoneModifier, boolean useMilliseconds) {
        this.zoneModifier = Optional.ofNullable(zoneModifier)
                                    .orElse(JacksonObjectMapperSupplier.ZONE_MODIFIER_USE_ORIGINAL);
        this.useMilliseconds = useMilliseconds;
    }

    protected JacksonUtilsZonedDateTimeSerializer(JacksonUtilsZonedDateTimeSerializer base,
                                                  Boolean useTimestamp,
                                                  DateTimeFormatter formatter,
                                                  Boolean writeZoneId) {
        this(base, useTimestamp, base._useNanoseconds, formatter, writeZoneId);
    }

    protected JacksonUtilsZonedDateTimeSerializer(JacksonUtilsZonedDateTimeSerializer base,
                                                  Boolean useTimestamp,
                                                  Boolean useNanoseconds,
                                                  DateTimeFormatter formatter,
                                                  Boolean writeZoneId) {
        super(base, useTimestamp, useNanoseconds, formatter, writeZoneId);
        zoneModifier = base.zoneModifier;
        useMilliseconds = base.useMilliseconds;
    }

    @Override
    protected JacksonUtilsZonedDateTimeSerializer withFormat(Boolean useTimestamp, DateTimeFormatter formatter, JsonFormat.Shape shape) {
        return new JacksonUtilsZonedDateTimeSerializer(this, useTimestamp, formatter, false);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected JacksonUtilsZonedDateTimeSerializer withFeatures(Boolean writeZoneId) {
        return new JacksonUtilsZonedDateTimeSerializer(this, _useTimestamp, _formatter, writeZoneId);
    }

    @Override
    protected JacksonUtilsZonedDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new JacksonUtilsZonedDateTimeSerializer(this, _useTimestamp, writeNanoseconds, _formatter, writeZoneId);
    }

    @Override
    @SuppressWarnings("PMD.AvoidReassigningParameters")
    protected String formatValue(ZonedDateTime value, SerializerProvider provider) {
        if (_formatter == null) {
            ZoneId zone = zoneModifier.apply(value.getZone());
            value = value.withZoneSameInstant(zone);
            value = useMilliseconds ? value : value.truncatedTo(ChronoUnit.SECONDS);
        }

        return super.formatValue(value, provider);
    }

}
