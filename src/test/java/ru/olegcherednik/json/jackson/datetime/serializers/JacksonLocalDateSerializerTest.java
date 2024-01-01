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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class JacksonLocalDateSerializerTest {

    private static final LocalDate LOCAL_DATE = LocalDate.parse("2023-12-10");

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LOCAL_DATE));
        assertThat(json).isEqualTo("{\"map\":{\"localDate\":\"2023-12-10\"}}");
    }

    public void shouldUseEpochDayWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LOCAL_DATE));
        assertThat(json).isEqualTo("{\"map\":{\"localDate\":19701}}");
    }

    public void shouldUseDateFormatWhenDateFormatNotNull() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(LOCAL_DATE));
        assertThat(json).isEqualTo("{\"map\":{\"localDate\":\"10-12-2023\"}}");
    }

    public void shouldSerializeTimestampWhenShapeNumberInt() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
            private final LocalDate localDate;

        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LOCAL_DATE));
        assertThat(json).isEqualTo("{\"localDate\":19701}");
    }

    public void shouldSerializeArrayWhenShapeNotNumberInt() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(shape = JsonFormat.Shape.ARRAY)
            private final LocalDate localDate;

        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LOCAL_DATE));
        assertThat(json).isEqualTo("{\"localDate\":[2023,12,10]}");
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, JacksonLocalDateSerializer.with(df));
        return module;
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<String, LocalDate> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<String, LocalDate> map) {
            this.map = map;
        }

        private Data(LocalDate value) {
            this(Collections.singletonMap("localDate", value));
        }

    }

}
