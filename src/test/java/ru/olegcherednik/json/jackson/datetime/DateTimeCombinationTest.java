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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.api.ZoneModifier;
import ru.olegcherednik.json.jackson.LocalZoneId;
import ru.olegcherednik.json.jackson.ResourceData;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals ")
public class DateTimeCombinationTest {

    public void shouldWriteDatesWithDefaultJsonSettings() throws IOException {
        DataOne data = new DataOne(ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00"));

        String actual = Json.writeValue(data);
        String expected = ResourceData.getResourceAsString("/datetime/def_date_one.json").trim();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadDatesWithDefaultJsonSettings() throws IOException {
        ZonedDateTime zdt = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");

        String json = ResourceData.getResourceAsString("/datetime/def_date_one.json").trim();
        DataOne actual = Json.readValue(json, DataOne.class);
        DataOne expected = new DataOne(zdt.toInstant(),
                                       zdt.toLocalDate(),
                                       zdt.toLocalTime(),
                                       zdt.toLocalDateTime(),
                                       zdt.toOffsetDateTime().toOffsetTime(),
                                       zdt.toOffsetDateTime(),
                                       zdt.withZoneSameInstant(ZoneId.systemDefault()),
                                       Date.from(zdt.toInstant()));
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldWriteDatesWithAnnotationSettingsPreferable() throws IOException {
        DataTwo data = new DataTwo(ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00"));
        String actual = Json.createWriter(getSettings()).writeValue(data);
        String expected = ResourceData.getResourceAsString("/datetime/custom_date_one.json").trim();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldReadDatesWithAnnotationSettingsPreferable() throws IOException {
        ZonedDateTime zdtLocal = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");
        ZonedDateTime zdtSingapore = zdtLocal.withZoneSameInstant(LocalZoneId.ASIA_SINGAPORE);

        String json = ResourceData.getResourceAsString("/datetime/custom_date_one.json").trim();

        DataTwo actual = Json.createReader(getSettings()).readValue(json, DataTwo.class);
        DataTwo expected = new DataTwo(zdtLocal.toInstant(),
                                       zdtLocal.toLocalDate(),
                                       zdtLocal.toLocalTime(),
                                       zdtLocal.toLocalDateTime(),
                                       zdtSingapore.toOffsetDateTime().toOffsetTime(),
                                       zdtSingapore.toOffsetDateTime(),
                                       zdtSingapore,
                                       Date.from(zdtLocal.toInstant()));


        assertThat(actual).isEqualTo(expected);
    }

    private static JsonSettings getSettings() {
        return JsonSettings.builder()
                           .zoneModifier(ZoneModifier.CONVERT_TO_UTC)
                           .instantFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd'T'HH:mm:ss.SSSz"))
                           .localTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'HH:mm:ss.SSS"))
                           .localDateFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd"))
                           .offsetTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'HH:mm:ss.SSSz"))
                           .offsetDateTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd'T'HH:mm:ss.SSSz"))
                           .zonedDateTimeFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd'T'HH:mm:ss.SSSz"))
                           .build();
    }

    public void shouldSerializeDatesWithAnnotationSettingsPreferable1() throws JsonProcessingException {
//        DateTimeFormatter df1 = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        DateTimeFormatter df3 = df2.withZone(ZoneOffset.UTC);

//        JsonSettings settings = JsonSettings.builder()
//                                            .zoneModifier(ZoneModifier.CONVERT_TO_UTC)
//                                           .instantFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
//                                           .localDateFormatter(DateTimeFormatter.ISO_DATE)
//                                           .localTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"))
//                                           .dateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
//                                           .offsetTimeFormatter(DateTimeFormatter.ISO_OFFSET_TIME)
//                                           .offsetDateTimeFormatter(df2.withZone(ZoneId.systemDefault()))
//                                           .zonedDateTimeFormatter(df2)
//                                            .build();

//        ZonedDateTime zonedDateTime = ZonedDateTime.parse("2023-12-03T10:39:20.187+03:00");
//        DataTwo data = new DataTwo(zonedDateTime);

//        String actual = Json.writeValue(data);

//        DataTwo data = new DataTwo(ZonedDateTime.parse("2023-12-03T07:39:20.187Z"));

//        String actual = Json.writeValue(data);
//        assertThat(actual).isEqualTo("{\"instant\":\"2023-12-03T10:39:20.187000+03:00\","
//                                             + "\"localTime\":\"07:39:20.187\","
//                                             + "\"localDate\":\"2023-12-03\","
//                                             + "\"localDateTime\":\"2023-12-03T07:39:20.187\","
//                                             + "\"offsetTime\":\"07:39:20.187Z\""
//                                             + ",\"offsetDateTime\":\"2023-12-03T07:39:20.187Z\""
//                                             + ",\"zonedDateTime\":\"2023-12-03T07:39:20.187Z\""
//                                             + ",\"date\":\"2023-12-03T10:39:20.187000+03:00\"}");

//        System.out.println(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(data));
//        System.out.println(Json.createPrettyPrint(settings).writeValue(data));
        // 2023-12-03T07:39:20.187 Z
        // 2023-12-03T10:39:20.187 MSK
        // 2023-12-03T15:39:20.187 SGT
    }

    @Getter
    @EqualsAndHashCode
    private static final class DataOne {

        private final Instant instant;
        private final LocalDate localDate;
        private final LocalTime localTime;
        private final LocalDateTime localDateTime;
        private final OffsetTime offsetTime;
        private final OffsetDateTime offsetDateTime;
        private final ZonedDateTime zonedDateTime;
        private final Date date;

        public DataOne(ZonedDateTime zonedDateTime) {
            this(zonedDateTime.toInstant(),
                 zonedDateTime.toLocalDate(),
                 zonedDateTime.toLocalTime(),
                 zonedDateTime.toLocalDateTime(),
                 zonedDateTime.toOffsetDateTime().toOffsetTime(),
                 zonedDateTime.toOffsetDateTime(),
                 zonedDateTime,
                 Date.from(zonedDateTime.toInstant()));
        }

        @JsonCreator
        public DataOne(@JsonProperty("instant") Instant instant,
                       @JsonProperty("localDate") LocalDate localDate,
                       @JsonProperty("localTime") LocalTime localTime,
                       @JsonProperty("localDateTime") LocalDateTime localDateTime,
                       @JsonProperty("offsetTime") OffsetTime offsetTime,
                       @JsonProperty("offsetDateTime") OffsetDateTime offsetDateTime,
                       @JsonProperty("zonedDateTime") ZonedDateTime zonedDateTime,
                       @JsonProperty("date") Date date) {
            this.instant = instant;
            this.localDate = localDate;
            this.localTime = localTime;
            this.localDateTime = localDateTime;
            this.offsetTime = offsetTime;
            this.offsetDateTime = offsetDateTime;
            this.zonedDateTime = zonedDateTime;
            this.date = new Date(date.getTime());
        }
    }

    @Getter
    @EqualsAndHashCode
    private static final class DataTwo {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Singapore")
        private final Instant instant;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd")
        private final LocalDate localDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'HH:mm:ss.SSS")
        private final LocalTime localTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSS")
        private final LocalDateTime localDateTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'HH:mm:ss.SSSXXX", timezone = "Asia/Singapore")
        private final OffsetTime offsetTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Singapore")
        private final OffsetDateTime offsetDateTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Singapore")
        private final ZonedDateTime zonedDateTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'[two] 'yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Singapore")
        private final Date date;

        public DataTwo(ZonedDateTime zonedDateTime) {
            this(zonedDateTime.toInstant(),
                 zonedDateTime.toLocalDate(),
                 zonedDateTime.toLocalTime(),
                 zonedDateTime.toLocalDateTime(),
                 zonedDateTime.toOffsetDateTime().toOffsetTime(),
                 zonedDateTime.toOffsetDateTime(),
                 zonedDateTime,
                 Date.from(zonedDateTime.toInstant()));
        }

        @JsonCreator
        public DataTwo(@JsonProperty("instant") Instant instant,
                       @JsonProperty("localDate") LocalDate localDate,
                       @JsonProperty("localTime") LocalTime localTime,
                       @JsonProperty("localDateTime") LocalDateTime localDateTime,
                       @JsonProperty("offsetTime") OffsetTime offsetTime,
                       @JsonProperty("offsetDateTime") OffsetDateTime offsetDateTime,
                       @JsonProperty("zonedDateTime") ZonedDateTime zonedDateTime,
                       @JsonProperty("date") Date date) {
            this.instant = instant;
            this.localDate = localDate;
            this.localTime = localTime;
            this.localDateTime = localDateTime;
            this.offsetTime = offsetTime;
            this.offsetDateTime = offsetDateTime;
            this.zonedDateTime = zonedDateTime;
            this.date = new Date(date.getTime());
        }

    }

}
