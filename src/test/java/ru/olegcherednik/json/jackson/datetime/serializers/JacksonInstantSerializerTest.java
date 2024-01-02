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
 * @since 23.12.2023
 */
@Test
public class JacksonInstantSerializerTest {

    private static final Instant INSTANT = Instant.parse("2023-12-10T19:22:40.758Z");
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(new Data(INSTANT));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":\"2023-12-10T19:22:40.758Z\"}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseNanosecondWhenWriteDateAsTimestampsAndWriteDateTimestampsAsNanoseconds()
            throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(new Data(INSTANT));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":1702236160.758000000}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeTimestampWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(new Data(INSTANT));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":1702236160758}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseDateFormatZoneWhenDateFormatHasZone() throws JsonProcessingException {
        DateTimeFormatter df = DF.withZone(LocalZoneId.ASIA_SINGAPORE);
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(new Data(INSTANT));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":\"2023-12-11T03:22:40.758+08:00\"}}");

        JsonSettings settings = JsonSettings.builder().instantFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseContextZoneWhenContextZoneExists() throws JsonProcessingException {
        SimpleModule module = createModule(DF);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        Data expected = new Data(INSTANT);
        String json = mapper.writeValueAsString(new Data(INSTANT));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":\"2023-12-11T03:22:40.758+08:00\"}}");

        JsonSettings settings = JsonSettings.builder().instantFormatter(DF).build();
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
        String json = mapper.writeValueAsString(new Data(INSTANT));
        assertThat(json).isEqualTo("{\"map\":{\"instant\":\"2023-12-10T19:22:40.758Z\"}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeTimestampWhenShapeNumberInt() throws JsonProcessingException {
        SimpleModule module = createModule(DF);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        DataInt expected = new DataInt();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"instant\":1702236160758}");

        DataInt actual = Json.readValue(json, DataInt.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeNanosecondsWhenShapeNumberFloat() throws JsonProcessingException {
        SimpleModule module = createModule(DF);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        DataFloat expected = new DataFloat();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"instant\":1702236160.758000000}");

        DataFloat actual = Json.readValue(json, DataFloat.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeTimestampWhenFeatureIsOn() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        DataNano expected = new DataNano();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"instant\":1702236160.758000000}");

        DataNano actual = Json.readValue(json, DataNano.class);
        assertThat(actual).isEqualTo(expected);
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

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataInt {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        private final Instant instant = INSTANT;

    }

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataFloat {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
        private final Instant instant = INSTANT;

    }

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataNano {

        @JsonFormat(with = JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        private final Instant instant = INSTANT;

    }

}
