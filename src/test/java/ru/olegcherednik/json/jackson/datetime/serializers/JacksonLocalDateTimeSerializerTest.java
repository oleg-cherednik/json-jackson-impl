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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class JacksonLocalDateTimeSerializerTest {

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalDateTime.parse("2023-12-10T19:22:40.758927")));
        assertThat(json).isEqualTo("{\"map\":{\"localDateTime\":\"2023-12-10T19:22:40.758927\"}}");
    }

    public void shouldUseArrayWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalDateTime.parse("2023-12-10T19:22:40.758927")));
        assertThat(json).isEqualTo("{\"map\":{\"localDateTime\":[2023,12,10,19,22,40,758927000]}}");
    }

    public void shouldUseDateFormatWhenDateFormatNotNull() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'ss:MM:yyyy");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(LocalDateTime.parse("2023-12-10T19:22:40.758927")));
        assertThat(json).isEqualTo("{\"map\":{\"localDateTime\":\"10-12-2023T40:12:2023\"}}");
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, JacksonLocalDateTimeSerializer.with(df));
        return module;
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<String, LocalDateTime> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<String, LocalDateTime> map) {
            this.map = map;
        }

        private Data(LocalDateTime value) {
            this(Collections.singletonMap("localDateTime", value));
        }

    }

}
