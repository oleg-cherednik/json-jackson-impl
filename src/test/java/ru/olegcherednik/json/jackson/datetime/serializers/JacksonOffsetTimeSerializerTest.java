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
import ru.olegcherednik.json.api.ZoneModifier;
import ru.olegcherednik.json.jackson.LocalZoneId;

import java.time.Instant;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class JacksonOffsetTimeSerializerTest {

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        OffsetTime offsetTime = OffsetTime.parse("22:16:19.989648300+03:00");
        String json = mapper.writeValueAsString(new Data(offsetTime));
        assertThat(json).isEqualTo("{\"map\":{\"offsetTime\":\"" + offsetTime + "\"}}");
    }

    public void shouldUseArrayWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetTime.parse("22:16:19.989648300+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"offsetTime\":[22,16,19,989648300,\"+03:00\"]}}");
    }

    public void shouldUseDateFormatWhenDateFormatNotNull() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[one]'hh:mm:ssXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetTime.parse("22:16:19.989648300+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"offsetTime\":\"[one]10:16:19+03:00\"}}");
    }

    public void shouldUseDateFormatZoneIdWhenDateFormatHasZoneId() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[one]'hh:mm:ss.SSSXXX")
                                                .withZone(LocalZoneId.ASIA_SINGAPORE);
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetTime.parse("22:16:19.989648300+03:00")));
        assertThat(json).isEqualTo("{\"map\":{\"offsetTime\":\"[one]03:16:19.989+08:00\"}}");
    }

    public void shouldSerializeArrayWhenShapeNumberInt() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
            private final OffsetTime offsetTime;

        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetTime.parse("22:16:19.989648300+03:00")));
        assertThat(json).isEqualTo("{\"offsetTime\":[22,16,19,989,\"+03:00\"]}");
    }

    public void shouldSerializeArrayWhenShapeNumberFloat() throws JsonProcessingException {
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
            private final OffsetTime offsetTime;

        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetTime.parse("22:16:19.989648300+03:00")));
        assertThat(json).isEqualTo("{\"offsetTime\":[22,16,19,989,\"+03:00\"]}");
    }

    public void shouldSerializeTimestampWhenFeatureIsOn() throws JsonProcessingException {
        /*
          Actually 'writeNanoseconds' is not used in serializer.
          This test is only for test coverage, because we should override this method to get proper instance.
         */
        @Getter
        @RequiredArgsConstructor
        class Data {

            @JsonFormat(with = JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            private final OffsetTime offsetTime;

        }

        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(OffsetTime.parse("22:16:19.989648300+03:00")));
        assertThat(json).isEqualTo("{\"offsetTime\":\"22:16:19.989648300+03:00\"}");
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(OffsetTime.class, JacksonOffsetTimeSerializer.with(df, ZoneModifier.USE_ORIGINAL));
        return module;
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<String, OffsetTime> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<String, OffsetTime> map) {
            this.map = map;
        }

        private Data(OffsetTime value) {
            this(Collections.singletonMap("offsetTime", value));
        }

    }

}
