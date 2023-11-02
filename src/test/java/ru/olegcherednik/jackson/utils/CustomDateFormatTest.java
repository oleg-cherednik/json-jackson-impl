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
package ru.olegcherednik.jackson.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.05.2022
 */
@Test
public class CustomDateFormatTest {

    public void shouldCreateJsonWithMsUtcDateWhenDefaultSettings() {
        Data data = createData();
        String json = JacksonUtils.writeValue(data);

        assertThat(json).isNotBlank();

        Map<String, String> actual = JacksonUtils.readMap(json, String.class, String.class);
        Map<String, String> expected = MapUtils.of("instant", "2017-07-23T13:57:14.225Z",
                                                   "localDateTime", "2017-07-23T13:57:14.225",
                                                   "localTime", "13:57:14.225",
                                                   "offsetDateTime", "2017-07-23T13:57:14.225Z",
                                                   "offsetTime", "13:57:14.225Z",
                                                   "zonedDateTime", "2017-07-23T13:57:14.225Z",
                                                   "date", "2017-07-23T13:57:14.225Z",
                                                   "customInstant", "23-07-2017 01:57:14.225 PM",
                                                   "customLocalDateTime", "23-07-2017 01:57:14.225 PM",
                                                   "customLocalTime", "01:57:14.225 PM",
                                                   "customOffsetDateTime", "23-07-2017 05:57:14.225 PM",
                                                   "customOffsetTime", "05:57:14.225 PM",
                                                   "customZonedDateTime", "23-07-2017 01:57:14.225 PM",
                                                   "customDate", "23-07-2017 01:57:14.225 PM");

        assertThat(actual).isEqualTo(expected);
    }

    public void shouldCreateJsonWithoutMsDateWhenNoMillisecondSettings() {
        Supplier<ObjectMapper> mapperSupplier = JacksonObjectMapperSupplier.builder()
                                                                           .withUseMilliseconds(false)
                                                                           .build();
        ObjectMapperDecorator jacksonUtils = JacksonUtilsHelper.createPrettyPrintMapperDecorator(mapperSupplier);
        Data data = createData();
        String json = jacksonUtils.writeValue(data);

        assertThat(json).isNotBlank();

        Map<String, String> actual = jacksonUtils.readMap(json, String.class, String.class);
        Map<String, String> expected = MapUtils.of("instant", "2017-07-23T13:57:14Z",
                                                   "localDateTime", "2017-07-23T13:57:14",
                                                   "localTime", "13:57:14",
                                                   "offsetDateTime", "2017-07-23T13:57:14Z",
                                                   "offsetTime", "13:57:14Z",
                                                   "zonedDateTime", "2017-07-23T13:57:14Z",
                                                   "date", "2017-07-23T13:57:14Z",
                                                   "customInstant", "23-07-2017 01:57:14.225 PM",
                                                   "customLocalDateTime", "23-07-2017 01:57:14.225 PM",
                                                   "customLocalTime", "01:57:14.225 PM",
                                                   "customOffsetDateTime", "23-07-2017 05:57:14.225 PM",
                                                   "customOffsetTime", "05:57:14.225 PM",
                                                   "customZonedDateTime", "23-07-2017 01:57:14.225 PM",
                                                   "customDate", "23-07-2017 01:57:14.225 PM");

        assertThat(actual).isEqualTo(expected);
    }

    public void shouldCreateJsonWithAsiaSingaporeDateWhenDefaultSettings() {
        Supplier<ObjectMapper> mapperSupplier = JacksonObjectMapperSupplier.builder()
                                                                           .zone(LocalZoneId.ASIA_SINGAPORE)
                                                                           .build();
        ObjectMapperDecorator jacksonUtils = JacksonUtilsHelper.createPrettyPrintMapperDecorator(mapperSupplier);
        Data data = createData();
        String json = jacksonUtils.writeValue(data);

        assertThat(json).isNotBlank();

        Map<String, String> actual = jacksonUtils.readMap(json, String.class, String.class);
        Map<String, String> expected = MapUtils.of("instant", "2017-07-23T21:57:14.225+08:00",
                                                   "localDateTime", "2017-07-23T13:57:14.225",
                                                   "localTime", "13:57:14.225",
                                                   "offsetDateTime", "2017-07-23T21:57:14.225+08:00",
                                                   "offsetTime", "21:57:14.225+08:00",
                                                   "zonedDateTime", "2017-07-23T21:57:14.225+08:00",
                                                   "date", "2017-07-23T21:57:14.225+08:00",
                                                   "customInstant", "23-07-2017 01:57:14.225 PM",
                                                   "customLocalDateTime", "23-07-2017 01:57:14.225 PM",
                                                   "customLocalTime", "01:57:14.225 PM",
                                                   "customOffsetDateTime", "23-07-2017 05:57:14.225 PM",
                                                   "customOffsetTime", "05:57:14.225 PM",
                                                   "customZonedDateTime", "23-07-2017 01:57:14.225 PM",
                                                   "customDate", "23-07-2017 01:57:14.225 PM");

        assertThat(actual).isEqualTo(expected);
    }

    private static Data createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        Date date = Date.from(ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)).toInstant());
        Instant instant = date.toInstant();
        OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.of("+04:00"));
        OffsetTime offsetTime = offsetDateTime.toOffsetTime();
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC));
        LocalDateTime localDateTime = LocalDateTime.parse(str, df);
        LocalTime localTime = localDateTime.toLocalTime();

        Data data = new Data();
        data.instant = instant;
        data.localDateTime = localDateTime;
        data.localTime = localTime;
        data.offsetDateTime = offsetDateTime;
        data.offsetTime = offsetTime;
        data.zonedDateTime = zonedDateTime;
        data.date = date;
        data.customInstant = instant;
        data.customLocalDateTime = localDateTime;
        data.customLocalTime = localTime;
        data.customOffsetDateTime = offsetDateTime;
        data.customOffsetTime = offsetTime;
        data.customZonedDateTime = zonedDateTime;
        data.customDate = date;

        return data;
    }

    private static final class Data {

        private static final String DATE_PATTERN = "dd-MM-yyyy";
        private static final String TIME_PATTERN = "hh:mm:ss.SSS a";
        private static final String DATE_TIME_PATTERN = DATE_PATTERN + ' ' + TIME_PATTERN;

        private Instant instant;
        private LocalDateTime localDateTime;
        private LocalTime localTime;
        private OffsetDateTime offsetDateTime;
        private OffsetTime offsetTime;
        private ZonedDateTime zonedDateTime;
        private Date date;

        @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = "UTC")
        private Instant customInstant;
        @JsonFormat(pattern = DATE_TIME_PATTERN)
        private LocalDateTime customLocalDateTime;
        @JsonFormat(pattern = TIME_PATTERN)
        private LocalTime customLocalTime;
        @JsonFormat(pattern = DATE_TIME_PATTERN)
        private OffsetDateTime customOffsetDateTime;
        @JsonFormat(pattern = TIME_PATTERN)
        private OffsetTime customOffsetTime;
        @JsonFormat(pattern = DATE_TIME_PATTERN)
        private ZonedDateTime customZonedDateTime;
        @JsonFormat(pattern = DATE_TIME_PATTERN)
        private Date customDate;


        /*
        addSerializer(Instant.class, InstantSerializer.INSTANCE);
        addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
        addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
        addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE);
        addSerializer(MonthDay.class, MonthDaySerializer.INSTANCE);
        addSerializer(OffsetDateTime.class, OffsetDateTimeSerializer.INSTANCE);
        addSerializer(OffsetTime.class, OffsetTimeSerializer.INSTANCE);
        addSerializer(Period.class, new ToStringSerializer(Period.class));
        addSerializer(Year.class, YearSerializer.INSTANCE);
        addSerializer(YearMonth.class, YearMonthSerializer.INSTANCE);

        /* 27-Jun-2015, tatu: This is the real difference from the old
         *  {@link JSR310Module}: default is to produce ISO-8601 compatible
         *  serialization with timezone offset only, not timezone id.
         *  But this is configurable.
         */
//        addSerializer(ZonedDateTime.class, ZonedDateTimeSerializer.INSTANCE);

        // since 2.11: need to override Type Id handling
        // (actual concrete type is `ZoneRegion`, but that's not visible)
//        addSerializer(ZoneId.class, new ZoneIdSerializer());
//        addSerializer(ZoneOffset.class, new ToStringSerializer(ZoneOffset.class));

//         */
    }
}
