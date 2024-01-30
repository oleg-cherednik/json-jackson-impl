/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    private static final LocalTime LOCAL_TIME = LocalTime.parse("19:22:40.758");

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        Data expected = new Data(LOCAL_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"localTime\":\"19:22:40.758\"}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        Data expected = new Data(LOCAL_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"localTime\":[19,22,40,758000000]}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseDateFormatWhenDateFormatNotNull() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("SSS.ss:mm:HH");
        SimpleModule module = createModule(df);
        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        Data expected = new Data(LOCAL_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"localTime\":\"758.40:22:19\"}}");

        JsonSettings settings = JsonSettings.builder().localTimeFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWhenShapeNumberInt() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[one]' HH:mm:ss.SSS");
        SimpleModule module = createModule(df);
        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        DataInt expected = new DataInt();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"localTime\":[19,22,40,758000000]}");

        DataInt actual = Json.readValue(json, DataInt.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWithIntWhenShapeNumberIntDisableNanosecond() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[two]' HH:mm:ss.SSS");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        DataInt expected = new DataInt();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"localTime\":[19,22,40,758]}");

        DataInt actual = Json.readValue(json, DataInt.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWhenShapeNumberFloat() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[three]' HH:mm:ss.SSS");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        DataFloat expected = new DataFloat();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"localTime\":[19,22,40,758000000]}");

        DataFloat actual = Json.readValue(json, DataFloat.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWhenShapeNotNumberInt() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[four]' HH:mm:ss.SSS");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        DataArray expected = new DataArray();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"localTime\":[19,22,40,758000000]}");

        DataArray actual = Json.readValue(json, DataArray.class);
        assertThat(actual).isEqualTo(expected);
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

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataInt {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        private final LocalTime localTime = LOCAL_TIME;

    }

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataFloat {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
        private final LocalTime localTime = LOCAL_TIME;

    }

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataArray {

        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        private final LocalTime localTime = LOCAL_TIME;

    }

}
