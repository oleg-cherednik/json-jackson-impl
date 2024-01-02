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

package ru.olegcherednik.json.jackson.datetime.serializers.key;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.jackson.LocalTimeZone;
import ru.olegcherednik.json.jackson.LocalZoneId;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 25.12.2023
 */
@Test
public class JacksonInstantKeySerializerTest {

    private static final Instant INSTANT = Instant.parse("2023-12-10T19:22:40.758Z");

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-10T19:22:40.758Z\":\"instant\"}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseNanosecondWhenWriteDateAsTimestampsAndWriteDateTimestampsAsNanoseconds()
            throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(INSTANT));
        assertThat(json).isEqualTo("{\"map\":{\"1702236160.758000000\":\"instant\"}}");
    }

    public void shouldSerializeTimestampWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(INSTANT));
        assertThat(json).isEqualTo("{\"map\":{\"1702236160758\":\"instant\"}}");
    }

    public void shouldUseDateFormatZoneWhenDateFormatHasZone() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                                                .withZone(LocalZoneId.ASIA_SINGAPORE);
        SimpleModule module = createModule(df);
        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-11T03:22:40.758+08:00\":\"instant\"}}");

        JsonSettings settings = JsonSettings.builder().instantFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseContextZoneWhenContextZoneExists() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-11T03:22:40.758+08:00\":\"instant\"}}");

        JsonSettings settings = JsonSettings.builder().instantFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldIgnoreContextZoneWhenDisableWriteDateWithContextTimeZone() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ISO_INSTANT;
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-10T19:22:40.758Z\":\"instant\"}}");

        JsonSettings settings = JsonSettings.builder().instantFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldIgnoreContextZoneWhenDisableWriteDateWithContextTimeZone1() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ISO_INSTANT;
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-10T19:22:40.758Z\":\"instant\"}}");

        JsonSettings settings = JsonSettings.builder().instantFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        SimpleModule module = new SimpleModule();
        module.addKeySerializer(Instant.class, new JacksonInstantKeySerializer(df));
        return module;
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<Instant, String> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<Instant, String> map) {
            this.map = map;
        }

        private Data(Instant key) {
            this(Collections.singletonMap(key, "instant"));
        }

    }

}
