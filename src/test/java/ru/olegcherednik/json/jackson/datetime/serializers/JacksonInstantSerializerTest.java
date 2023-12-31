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
import ru.olegcherednik.json.jackson.LocalTimeZone;
import ru.olegcherednik.json.jackson.LocalZoneId;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class JacksonInstantSerializerTest {

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(Instant.parse("2023-12-10T19:22:40.758927Z")));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":\"2023-12-10T19:22:40.758927Z\"}}");
    }

    public void shouldUseNanosecondWhenWriteDateAsTimestampsAndWriteDateTimestampsAsNanoseconds()
            throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(Instant.parse("2023-12-10T19:22:40.758927Z")));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":1702236160.758927000}}");
    }

    public void shouldSerializeTimestampWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(Instant.parse("2023-12-10T19:22:40.758927Z")));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":1702236160758}}");
    }

    public void shouldUseDateFormatZoneWhenDateFormatHasZone() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                                                .withZone(LocalZoneId.ASIA_SINGAPORE);
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(Instant.parse("2023-12-10T19:22:40.758927Z")));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":\"2023-12-11T03:22:40.758+08:00\"}}");
    }

    public void shouldUseContextZoneWhenContextZoneExists() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(Instant.parse("2023-12-10T19:22:40.758927Z")));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":\"2023-12-11T03:22:40.758+08:00\"}}");
    }

    public void shouldIgnoreContextZoneWhenDisableWriteDateWithContextTimeZone() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ISO_INSTANT;
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(Instant.parse("2023-12-10T19:22:40.758927Z")));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":\"2023-12-10T19:22:40.758927Z\"}}");
    }

    public void shouldSerializeTimestampWhenShapeNumberInt() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
            private final Instant instant;

        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(Instant.parse("2023-12-10T19:22:40.758927Z")));
        assertThat(json).isEqualTo("{\"instant\":1702236160758}");
    }

    public void shouldSerializeNanosecondsWhenShapeNumberFloat() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
            private final Instant instant;

        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(Instant.parse("2023-12-10T19:22:40.758927Z")));
        assertThat(json).isEqualTo("{\"instant\":1702236160.758927000}");
    }

    public void shouldSerializeTimestampWhenFeatureIsOn() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(with = JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            private final Instant instant;

        }

        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(Instant.parse("2023-12-10T19:22:40.758927Z")));
        assertThat(json).isEqualTo("{\"instant\":1702236160.758927000}");
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        JacksonInstantSerializer ser = new JacksonInstantSerializer(df);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, ser);
        return module;
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<String, Instant> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<String, Instant> map) {
            this.map = map;
        }

        private Data(Instant value) {
            this(Collections.singletonMap("instant", value));
        }

    }

}
