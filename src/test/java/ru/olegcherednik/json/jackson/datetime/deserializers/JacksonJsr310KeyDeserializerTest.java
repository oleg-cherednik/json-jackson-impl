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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.jackson.LocalZoneId;
import ru.olegcherednik.json.jackson.datetime.modules.JacksonJavaTimeModule;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 24.12.2023
 */
//@Test
public class JacksonJsr310KeyDeserializerTest {

    public void foo() throws JsonProcessingException {
        ZonedDateTime zdtLocal = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");
        ZonedDateTime zdtSingapore = zdtLocal.withZoneSameInstant(LocalZoneId.ASIA_SINGAPORE);

        Data expected = new Data(zdtLocal.toInstant(),
                                 zdtLocal.toLocalDate(),
                                 zdtLocal.toLocalTime(),
                                 zdtLocal.toLocalDateTime(),
                                 zdtSingapore.toOffsetDateTime().toOffsetTime(),
                                 zdtSingapore.toOffsetDateTime(),
                                 zdtSingapore);

        new JavaTimeModule();
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JacksonJavaTimeModule(JsonSettings.DEFAULT.getInstantFormatter(),
                                                          JsonSettings.DEFAULT.getLocalDateFormatter(),
                                                          JsonSettings.DEFAULT.getLocalTimeFormatter(),
                                                          JsonSettings.DEFAULT.getLocalDateTimeFormatter(),
                                                          JsonSettings.DEFAULT.getOffsetTimeFormatter(),
                                                          JsonSettings.DEFAULT.getOffsetDateTimeFormatter(),
                                                          JsonSettings.DEFAULT.getZonedDateTimeFormatter(),
                                                          JsonSettings.DEFAULT.getZoneModifier()));

        System.out.println(objectMapper.writeValueAsString(Collections.singletonMap(zdtLocal.minusYears(5).toInstant(), zdtLocal.toInstant())));

    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<Instant, Instant> instant;
        private final Map<LocalDate, LocalDate> localDate;
        private final Map<LocalTime, LocalTime> localTime;
        private final Map<LocalDateTime, LocalDateTime> localDateTime;
        private final Map<OffsetTime, OffsetTime> offsetTime;
        private final Map<OffsetDateTime, OffsetDateTime> offsetDateTime;
        private final Map<ZonedDateTime, ZonedDateTime> zonedDateTime;

        @JsonCreator
        private Data(@JsonProperty("instant") Map<Instant, Instant> instant,
                     @JsonProperty("localDate") Map<LocalDate, LocalDate> localDate,
                     @JsonProperty("localTime") Map<LocalTime, LocalTime> localTime,
                     @JsonProperty("localDateTime") Map<LocalDateTime, LocalDateTime> localDateTime,
                     @JsonProperty("offsetTime") Map<OffsetTime, OffsetTime> offsetTime,
                     @JsonProperty("offsetDateTime") Map<OffsetDateTime, OffsetDateTime> offsetDateTime,
                     @JsonProperty("zonedDateTime") Map<ZonedDateTime, ZonedDateTime> zonedDateTime) {
            this.instant = instant;
            this.localDate = localDate;
            this.localTime = localTime;
            this.localDateTime = localDateTime;
            this.offsetTime = offsetTime;
            this.offsetDateTime = offsetDateTime;
            this.zonedDateTime = zonedDateTime;
        }

        private Data(Instant instant,
                     LocalDate localDate,
                     LocalTime localTime,
                     LocalDateTime localDateTime,
                     OffsetTime offsetTime,
                     OffsetDateTime offsetDateTime,
                     ZonedDateTime zonedDateTime) {
            this(map(instant),
                 Collections.singletonMap(localDate, localDate),
                 Collections.singletonMap(localTime, localTime),
                 Collections.singletonMap(localDateTime, localDateTime),
                 Collections.singletonMap(offsetTime, offsetTime),
                 Collections.singletonMap(offsetDateTime, offsetDateTime),
                 Collections.singletonMap(zonedDateTime, zonedDateTime));
        }

        private static <T> Map<T, T> map(T key) {
            Map<T, T> map = new HashMap<>();
            map.put(key, key);
            return map;
        }

    }

}
