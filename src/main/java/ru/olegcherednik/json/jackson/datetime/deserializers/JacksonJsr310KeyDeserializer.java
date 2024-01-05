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

package ru.olegcherednik.json.jackson.datetime.deserializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;

/**
 * @param <T> type of the instance
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
@RequiredArgsConstructor
public class JacksonJsr310KeyDeserializer<T> extends KeyDeserializer {

    protected final Class<T> type;
    protected final TemporalQuery<T> query;
    protected final DateTimeFormatter df;

    public static JacksonJsr310KeyDeserializer<Instant> instant(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(Instant.class, Instant::from, df);
    }

    public static JacksonJsr310KeyDeserializer<LocalDate> localDate(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(LocalDate.class, LocalDate::from, df);
    }

    public static JacksonJsr310KeyDeserializer<LocalTime> localTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(LocalTime.class, LocalTime::from, df);
    }

    public static JacksonJsr310KeyDeserializer<LocalDateTime> localDateTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(LocalDateTime.class, LocalDateTime::from, df);
    }

    public static JacksonJsr310KeyDeserializer<OffsetTime> offsetTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(OffsetTime.class, OffsetTime::from, df);
    }

    public static JacksonJsr310KeyDeserializer<OffsetDateTime> offsetDateTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(OffsetDateTime.class, OffsetDateTime::from, df);
    }

    public static JacksonJsr310KeyDeserializer<ZonedDateTime> zonedDateTime(DateTimeFormatter df) {
        return new JacksonJsr310KeyDeserializer<>(ZonedDateTime.class, ZonedDateTime::from, df);
    }

    @Override
    public final Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return "".equals(key) ? null : df.parse(key, query);
    }
}

