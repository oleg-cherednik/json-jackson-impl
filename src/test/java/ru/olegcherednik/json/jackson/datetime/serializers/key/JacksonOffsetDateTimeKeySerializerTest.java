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
import ru.olegcherednik.json.jackson.LocalTimeZone;
import ru.olegcherednik.json.jackson.LocalZoneId;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.01.2024
 */
@Test
@SuppressWarnings("NewClassNamingConvention")
public class JacksonOffsetDateTimeKeySerializerTest {

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetDateTime.parse("2023-12-10T16:22:40.758927+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-10T16:22:40.758927+03:00\":\"offsetDateTime\"}}");
    }

    public void shouldUseNanosecondWhenWriteDateAsTimestampsAndWriteDateTimestampsAsNanoseconds()
            throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetDateTime.parse("2023-12-10T16:22:40.758927+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"1702214560.758927000\":\"offsetDateTime\"}}");
    }

    public void shouldSerializeTimestampWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetDateTime.parse("2024-01-01T06:36:12.870708100+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"1704080172870\":\"offsetDateTime\"}}");
    }

    public void shouldUseDateFormatZoneWhenDateFormatHasZone() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                                                .withZone(LocalZoneId.ASIA_SINGAPORE);
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetDateTime.parse("2024-01-01T06:36:12.870708100+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"2024-01-01T11:36:12.870+08:00\":\"offsetDateTime\"}}");
    }

    public void shouldUseContextZoneWhenContextZoneExists() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetDateTime.parse("2024-01-01T06:36:12.870708100+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"2024-01-01T11:36:12.870+08:00\":\"offsetDateTime\"}}");
    }

    public void shouldIgnoreContextZoneWhenDisableWriteDateWithContextTimeZone() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetDateTime.parse("2024-01-01T06:36:12.870708100+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"2024-01-01T06:36:12.8707081+03:00\":\"offsetDateTime\"}}");
    }

    public void shouldIgnoreContextZoneWhenDisableWriteDateWithContextTimeZone1() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetDateTime.parse("2024-01-01T06:36:12.870708100+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"2024-01-01T06:36:12.8707081+03:00\":\"offsetDateTime\"}}");
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        SimpleModule module = new SimpleModule();
        module.addKeySerializer(OffsetDateTime.class, new JacksonOffsetDateTimeKeySerializer(df));
        return module;
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<OffsetDateTime, String> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<OffsetDateTime, String> map) {
            this.map = map;
        }

        private Data(OffsetDateTime key) {
            this(Collections.singletonMap(key, "offsetDateTime"));
        }

    }

}
