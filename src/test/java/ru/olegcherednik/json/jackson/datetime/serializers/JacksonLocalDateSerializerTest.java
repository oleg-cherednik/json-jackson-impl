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

        Data expected = new Data(LOCAL_DATE);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"localDate\":\"2023-12-10\"}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        Data expected = new Data(LOCAL_DATE);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"localDate\":[2023,12,10]}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseDateFormatWhenDateFormatNotNull() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        Data expected = new Data(LOCAL_DATE);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"localDate\":\"10-12-2023\"}}");

        JsonSettings settings = JsonSettings.builder().localDateFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeEpochDayWhenWriteDateAsTimestampsAndShapeNumberInt() throws JsonProcessingException {
        SimpleModule module = createModule(DateTimeFormatter.ISO_LOCAL_DATE);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        DataInt expected = new DataInt();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"localDate\":19701}");

        DataInt actual = Json.readValue(json, DataInt.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWhenWriteDateAsTimestampsAndShapeNumberFloat() throws JsonProcessingException {
        SimpleModule module = createModule(DateTimeFormatter.ISO_LOCAL_DATE);
        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        DataFloat expected = new DataFloat();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"localDate\":[2023,12,10]}");

        DataFloat actual = Json.readValue(json, DataFloat.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWhenShapeNumberArray() throws JsonProcessingException {
        SimpleModule module = createModule(DateTimeFormatter.ISO_LOCAL_DATE);
        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        DataArray expected = new DataArray();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"localDate\":[2023,12,10]}");

        DataArray actual = Json.readValue(json, DataArray.class);
        assertThat(actual).isEqualTo(expected);
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

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataInt {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        private final LocalDate localDate = LOCAL_DATE;

    }

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataFloat {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
        private final LocalDate localDate = LOCAL_DATE;

    }

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataArray {

        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        private final LocalDate localDate = LOCAL_DATE;

    }

}
