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
package ru.olegcherednik.jackson_utils.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import ru.olegcherednik.jackson_utils.JacksonObjectMapperSupplier;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 02.05.2022
 */
public class JacksonUtilsOffsetDateTimeSerializer extends OffsetDateTimeSerializer {

    private static final long serialVersionUID = -3716814686096490966L;

    private final UnaryOperator<ZoneId> zoneModifier;
    private final boolean useMilliseconds;

    public JacksonUtilsOffsetDateTimeSerializer(UnaryOperator<ZoneId> zoneModifier, boolean useMilliseconds) {
        this.zoneModifier = Optional.ofNullable(zoneModifier)
                                    .orElse(JacksonObjectMapperSupplier.ZONE_MODIFIER_USE_ORIGINAL);
        this.useMilliseconds = useMilliseconds;
    }

    protected JacksonUtilsOffsetDateTimeSerializer(JacksonUtilsOffsetDateTimeSerializer base,
                                                   Boolean useTimestamp,
                                                   DateTimeFormatter formatter) {
        super(base, useTimestamp, formatter);
        zoneModifier = base.zoneModifier;
        useMilliseconds = base.useMilliseconds;
    }

    protected JacksonUtilsOffsetDateTimeSerializer(JacksonUtilsOffsetDateTimeSerializer base,
                                                   Boolean useTimestamp,
                                                   Boolean useNanoseconds,
                                                   DateTimeFormatter formatter) {
        super(base, useTimestamp, useNanoseconds, formatter);
        zoneModifier = base.zoneModifier;
        useMilliseconds = base.useMilliseconds;
    }

    @Override
    protected JacksonUtilsOffsetDateTimeSerializer withFormat(Boolean useTimestamp,
                                                              DateTimeFormatter formatter,
                                                              JsonFormat.Shape shape) {
        return new JacksonUtilsOffsetDateTimeSerializer(this, useTimestamp, formatter);
    }

    @Override
    protected JacksonUtilsOffsetDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean useNanoseconds) {
        return new JacksonUtilsOffsetDateTimeSerializer(this, _useTimestamp, useNanoseconds, _formatter);
    }

    @Override
    @SuppressWarnings("PMD.AvoidReassigningParameters")
    protected String formatValue(OffsetDateTime value, SerializerProvider provider) {
        if (_formatter == null) {
            ZoneId zone = zoneModifier.apply(value.getOffset());
            ZoneOffset offset = zone.getRules().getOffset(value.toInstant());
            value = value.withOffsetSameInstant(offset);
            value = useMilliseconds ? value : value.truncatedTo(ChronoUnit.SECONDS);
        }

        return super.formatValue(value, provider);
    }

}
