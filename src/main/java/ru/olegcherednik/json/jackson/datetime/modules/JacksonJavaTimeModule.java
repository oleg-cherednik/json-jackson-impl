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

package ru.olegcherednik.json.jackson.datetime.modules;

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleKeyDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.json.jackson.datetime.deserializers.JacksonInstantDeserializer;
import ru.olegcherednik.json.jackson.datetime.deserializers.JacksonJsr310KeyDeserializer;
import ru.olegcherednik.json.jackson.datetime.deserializers.JacksonOffsetDateTimeDeserializer;
import ru.olegcherednik.json.jackson.datetime.deserializers.JacksonOffsetTimeDeserializer;
import ru.olegcherednik.json.jackson.datetime.deserializers.JacksonZonedDateTimeDeserializer;
import ru.olegcherednik.json.jackson.datetime.serializers.JacksonInstantSerializer;
import ru.olegcherednik.json.jackson.datetime.serializers.JacksonLocalDateSerializer;
import ru.olegcherednik.json.jackson.datetime.serializers.JacksonLocalDateTimeSerializer;
import ru.olegcherednik.json.jackson.datetime.serializers.JacksonLocalTimeSerializer;
import ru.olegcherednik.json.jackson.datetime.serializers.JacksonOffsetDateTimeSerializer;
import ru.olegcherednik.json.jackson.datetime.serializers.JacksonOffsetTimeSerializer;
import ru.olegcherednik.json.jackson.datetime.serializers.JacksonZonedDateTimeSerializer;

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

    protected final transient DateTimeFormatter instant;
    protected final transient DateTimeFormatter localDate;
    protected final transient DateTimeFormatter localTime;
    protected final transient DateTimeFormatter localDateTime;
    protected final transient DateTimeFormatter offsetTime;
    protected final transient DateTimeFormatter offsetDateTime;
    protected final transient DateTimeFormatter zonedDateTime;
    protected final UnaryOperator<ZoneId> zoneModifier;

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        addSerializers(context);
        addKeyDeserializers(context);
        addDeserializers(context);
    }

    private void addSerializers(SetupContext context) {
        SimpleSerializers ser = new SimpleSerializers();

        ser.addSerializer(Instant.class, new JacksonInstantSerializer(instant, zoneModifier));
        ser.addSerializer(LocalDate.class, JacksonLocalDateSerializer.with(localDate));
        ser.addSerializer(LocalTime.class, JacksonLocalTimeSerializer.with(localTime));
        ser.addSerializer(LocalDateTime.class, JacksonLocalDateTimeSerializer.with(localDateTime));
        ser.addSerializer(OffsetTime.class, JacksonOffsetTimeSerializer.with(offsetTime, zoneModifier));
        ser.addSerializer(OffsetDateTime.class, JacksonOffsetDateTimeSerializer.with(offsetDateTime,
                                                                                     zoneModifier));
        ser.addSerializer(ZonedDateTime.class, JacksonZonedDateTimeSerializer.with(zonedDateTime,
                                                                                   zoneModifier));

        context.addKeySerializers(ser);
        context.addSerializers(ser);
    }

    private void addKeyDeserializers(SetupContext context) {
        SimpleKeyDeserializers des = new SimpleKeyDeserializers();

        des.addDeserializer(Instant.class, JacksonJsr310KeyDeserializer.instant(instant));
        des.addDeserializer(LocalDate.class, JacksonJsr310KeyDeserializer.localDate(localDate));
        des.addDeserializer(LocalTime.class, JacksonJsr310KeyDeserializer.localTime(localTime));
        des.addDeserializer(LocalDateTime.class, JacksonJsr310KeyDeserializer.localDateTime(localDateTime));
        des.addDeserializer(OffsetTime.class, JacksonJsr310KeyDeserializer.offsetTime(offsetTime));
        des.addDeserializer(OffsetDateTime.class, JacksonJsr310KeyDeserializer.offsetDateTime(offsetDateTime));
        des.addDeserializer(ZonedDateTime.class, JacksonJsr310KeyDeserializer.zonedDateTime(zonedDateTime));

        context.addKeyDeserializers(des);
    }

    private void addDeserializers(SetupContext context) {
        SimpleDeserializers des = new SimpleDeserializers();

        des.addDeserializer(Instant.class, new JacksonInstantDeserializer(instant));
        des.addDeserializer(LocalDate.class, new LocalDateDeserializer(localDate));
        des.addDeserializer(LocalTime.class, new LocalTimeDeserializer(localTime));
        des.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(localDateTime));
        des.addDeserializer(OffsetTime.class, new JacksonOffsetTimeDeserializer(offsetTime));
        des.addDeserializer(OffsetDateTime.class, new JacksonOffsetDateTimeDeserializer(offsetDateTime));
        des.addDeserializer(ZonedDateTime.class, new JacksonZonedDateTimeDeserializer(zonedDateTime));

        context.addDeserializers(des);
    }

}

