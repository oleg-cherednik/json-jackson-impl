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

package ru.olegcherednik.json.jackson.datetime.serializers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class JacksonLocalTimeSerializerTest {

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalTime.parse("19:22:40.758927")));
        assertThat(json).isEqualTo("{\"map\":{\"localTime\":\"19:22:40.758927\"}}");
    }

    public void shouldUseNanoOfDayWhenWriteDateAsTimestampsNano() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalTime.parse("19:22:40.758927")));
        assertThat(json).isEqualTo("{\"map\":{\"localTime\":69760758927000}}");
    }

    public void shouldUseSecondOfDayWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalTime.parse("19:22:40.758927")));
        assertThat(json).isEqualTo("{\"map\":{\"localTime\":69760}}");
    }

    public void shouldUseDateFormatWhenDateFormatNotNull() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("SSS.ss:mm:HH");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalTime.parse("19:22:40.758927")));
        assertThat(json).isEqualTo("{\"map\":{\"localTime\":\"758.40:22:19\"}}");
    }

    public void shouldSerializeTimestampWhenShapeNumberInt() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
            private final LocalTime localTime;

        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[one]' HH:mm:ss.SSS");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalTime.parse("19:22:40.758927")));
        assertThat(json).isEqualTo("{\"localTime\":69760}");
    }

    public void shouldSerializeNanoWhenShapeNotNumberInt() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
            private final LocalTime localTime;

        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[one]' HH:mm:ss.SSS");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalTime.parse("19:22:40.758927")));
        assertThat(json).isEqualTo("{\"localTime\":69760758927000}");
    }

    public void shouldSerializeArrayWhenShapeNotNumberInt() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(shape = JsonFormat.Shape.ARRAY)
            private final LocalTime localTime;

        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[one]' HH:mm:ss.SSS");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalTime.parse("19:22:40.758927")));
        assertThat(json).isEqualTo("{\"localTime\":[19,22,40,758]}");
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalTime.class, JacksonLocalTimeSerializer.with(df));
        return module;
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<String, LocalTime> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<String, LocalTime> map) {
            this.map = map;
        }

        private Data(LocalTime value) {
            this(Collections.singletonMap("localTime", value));
        }

    }

}
