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
import ru.olegcherednik.json.jackson.LocalZoneId;
import ru.olegcherednik.json.jackson.LocalZoneOffset;

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

    private static final OffsetTime OFFSET_TIME = OffsetTime.parse("22:16:19.989+03:00");

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        Data expected = new Data(OFFSET_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"offsetTime\":\"" + OFFSET_TIME + "\"}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseArrayWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        Data expected = new Data(OFFSET_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"offsetTime\":[22,16,19,989000000,\"+03:00\"]}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseDateFormatWhenDateFormatNotNull() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[one]'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);
        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        Data expected = new Data(OFFSET_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"offsetTime\":\"[one]22:16:19.989+03:00\"}}");

        JsonSettings settings = JsonSettings.builder().offsetTimeFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseDateFormatZoneIdWhenDateFormatHasZoneId() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[one]'HH:mm:ss.SSSXXX")
                                                .withZone(LocalZoneId.ASIA_SINGAPORE);
        SimpleModule module = createModule(df);
        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(OFFSET_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"offsetTime\":\"[one]03:16:19.989+08:00\"}}");

        JsonSettings settings = JsonSettings.builder().offsetTimeFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        Data expected = new Data(OFFSET_TIME.withOffsetSameInstant(LocalZoneOffset.ASIA_SINGAPORE));
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWhenShapeNumberInt() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        DataInt expected = new DataInt();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"offsetTime\":[22,16,19,989,\"+03:00\"]}");

        DataInt actual = Json.readValue(json, DataInt.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeArrayWhenShapeNumberFloat() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        DataFloat expected = new DataFloat();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"offsetTime\":[22,16,19,989,\"+03:00\"]}");

        DataFloat actual = Json.readValue(json, DataFloat.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeTimestampWhenFeatureIsOn() throws JsonProcessingException {
        /*
          Actually 'writeNanoseconds' is not used in serializer.
          This test is only for test coverage, because we should override this method to get proper instance.
         */
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        DataNano expected = new DataNano();
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"offsetTime\":\"22:16:19.989+03:00\"}");

        DataNano actual = Json.readValue(json, DataNano.class);
        assertThat(actual).isEqualTo(expected);
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(OffsetTime.class, JacksonOffsetTimeSerializer.with(df));
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

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataInt {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        private final OffsetTime offsetTime = OFFSET_TIME;

    }

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataFloat {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
        private final OffsetTime offsetTime = OFFSET_TIME;

    }

    @EqualsAndHashCode
    @SuppressWarnings("PMD.UnusedPrivateField")
    private static final class DataNano {

        @JsonFormat(with = JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        private final OffsetTime offsetTime = OFFSET_TIME;

    }

}
