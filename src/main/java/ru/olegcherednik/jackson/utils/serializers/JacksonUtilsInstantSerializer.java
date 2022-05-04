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
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import ru.olegcherednik.jackson.utils.JacksonObjectMapperSupplier;

import java.time.Instant;
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
public class JacksonUtilsInstantSerializer extends InstantSerializer {

    private static final long serialVersionUID = 4402811900481017128L;

    private final UnaryOperator<ZoneId> zoneModifier;
    private final boolean useMilliseconds;

    public JacksonUtilsInstantSerializer(UnaryOperator<ZoneId> zoneModifier, boolean useMilliseconds) {
        this.zoneModifier = Optional.ofNullable(zoneModifier)
                                    .orElse(JacksonObjectMapperSupplier.ZONE_MODIFIER_USE_ORIGINAL);
        this.useMilliseconds = useMilliseconds;
    }

    protected JacksonUtilsInstantSerializer(JacksonUtilsInstantSerializer base,
                                            Boolean useTimestamp,
                                            DateTimeFormatter formatter) {
        this(base, useTimestamp, base._useNanoseconds, formatter);
    }

    protected JacksonUtilsInstantSerializer(JacksonUtilsInstantSerializer base,
                                            Boolean useTimestamp,
                                            Boolean useNanoseconds,
                                            DateTimeFormatter formatter) {
        super(base, useTimestamp, useNanoseconds, formatter);
        zoneModifier = base.zoneModifier;
        useMilliseconds = base.useMilliseconds;
    }

    @Override
    protected JacksonUtilsInstantSerializer withFormat(Boolean useTimestamp,
                                                       DateTimeFormatter formatter,
                                                       JsonFormat.Shape shape) {
        return new JacksonUtilsInstantSerializer(this, useTimestamp, formatter);
    }

    @Override
    protected JacksonUtilsInstantSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new JacksonUtilsInstantSerializer(this, _useTimestamp, writeNanoseconds, _formatter);
    }

    @Override
    protected String formatValue(Instant value, SerializerProvider provider) {
        if (_formatter == null) {
            value = useMilliseconds ? value : value.truncatedTo(ChronoUnit.SECONDS);
            ZoneId zone = zoneModifier.apply(ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(zone);
            return formatter.format(value);
        }

        return super.formatValue(value, provider);
    }

}
