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

package ru.olegcherednik.json.jackson.datetime;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 14.12.2023
 */
@RequiredArgsConstructor
public class JacksonJavaTimeModule extends SimpleModule {

    private static final long serialVersionUID = 7443855953089866124L;

    protected final transient DateTimeFormatter instantFormatter;
    protected final transient DateTimeFormatter localDateFormatter;
    protected final transient DateTimeFormatter localTimeFormatter;
    protected final transient DateTimeFormatter localDateTimeFormatter;
    protected final transient DateTimeFormatter offsetTimeFormatter;
    protected final transient DateTimeFormatter offsetDateTimeFormatter;
    protected final UnaryOperator<ZoneId> zoneModifier;

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        JacksonInstantSerializer instant = JacksonInstantSerializer.INSTANCE.with(instantFormatter, zoneModifier);
        SimpleSerializers serializers = new SimpleSerializers();

        serializers.addSerializer(Instant.class, instant);
        serializers.addSerializer(LocalDate.class, JacksonLocalDateSerializer.INSTANCE.with(localDateFormatter));
        serializers.addSerializer(LocalTime.class, JacksonLocalTimeSerializer.INSTANCE.with(localTimeFormatter));
        serializers.addSerializer(LocalDateTime.class, JacksonLocalDateTimeSerializer.INSTANCE
                .with(localDateTimeFormatter));
        serializers.addSerializer(OffsetTime.class,
                                  JacksonOffsetTimeSerializer.INSTANCE.with(offsetTimeFormatter, zoneModifier));
        serializers.addSerializer(OffsetDateTime.class, JacksonOffsetDateTimeSerializer.INSTANCE
                .with(offsetDateTimeFormatter, zoneModifier));
        serializers.addSerializer(ZonedDateTime.class, JacksonZonedDateTimeSerializer.INSTANCE
                .with(offsetDateTimeFormatter, zoneModifier));

        context.addSerializers(serializers);
        context.addKeySerializers(serializers);
    }

}
