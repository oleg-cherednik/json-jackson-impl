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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.jackson.LocalTimeZone;
import ru.olegcherednik.json.jackson.LocalZoneId;
import ru.olegcherednik.json.jackson.LocalZoneOffset;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class JacksonZonedDateTimeSerializerTest {

    private static final ZonedDateTime ZONED_DATE_TIME =
            ZonedDateTime.parse("2023-12-23T22:20:36.855+03:00[Europe/Moscow]");
    private static final ZonedDateTime ZONED_DATE_TIME_UTC = ZONED_DATE_TIME.withZoneSameInstant(ZoneOffset.UTC);
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        Data expected = new Data(ZONED_DATE_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"zonedDateTime\":\"2023-12-23T22:20:36.855+03:00[Europe/Moscow]\"}}");

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

        String json = mapper.writeValueAsString(new Data(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"zonedDateTime\":1703359236.855000000}}");

        Data actual = Json.readValue(json, Data.class);
        Data expected = new Data(ZONED_DATE_TIME_UTC);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeTimestampWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"zonedDateTime\":1703359236855}}");

        Data actual = Json.readValue(json, Data.class);
        Data expected = new Data(ZONED_DATE_TIME_UTC);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseDateFormatZoneWhenDateFormatHasZone() throws JsonProcessingException {
        DateTimeFormatter df = DF.withZone(LocalZoneId.ASIA_SINGAPORE);
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"zonedDateTime\":\"2023-12-24T03:20:36.855+08:00\"}}");

        Data actual = Json.readValue(json, Data.class);
        Data expected = new Data(ZONED_DATE_TIME.toOffsetDateTime()
                                                .withOffsetSameInstant(LocalZoneOffset.ASIA_SINGAPORE)
                                                .toZonedDateTime());
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseContextZoneWhenContextZoneExists() throws JsonProcessingException {
        SimpleModule module = createModule(DF);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"zonedDateTime\":\"2023-12-24T03:20:36.855+08:00\"}}");

        Data actual = Json.readValue(json, Data.class);
        Data expected = new Data(ZONED_DATE_TIME.toOffsetDateTime()
                                                .withOffsetSameInstant(LocalZoneOffset.ASIA_SINGAPORE)
                                                .toZonedDateTime());
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldIgnoreContextZoneWhenDisableWriteDateWithContextTimeZone() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ISO_INSTANT;
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"zonedDateTime\":\"2023-12-23T19:20:36.855Z\"}}");

        Data actual = Json.readValue(json, Data.class);
        Data expected = new Data(ZONED_DATE_TIME_UTC);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeTimestampWhenShapeNumberInt() throws JsonProcessingException {
        SimpleModule module = createModule(DF);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new DataInt(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"zonedDateTime\":1703359236855}");

        DataInt actual = Json.readValue(json, DataInt.class);
        DataInt expected = new DataInt(ZONED_DATE_TIME_UTC);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeNanosecondsWhenShapeNumberFloat() throws JsonProcessingException {
        SimpleModule module = createModule(DF);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new DataFloat(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"zonedDateTime\":1703359236.855000000}");

        DataFloat actual = Json.readValue(json, DataFloat.class);
        DataFloat expected = new DataFloat(ZONED_DATE_TIME_UTC);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldSerializeTimestampWhenFeatureIsOn() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new DataNano(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"zonedDateTime\":1703359236.855000000}");

        DataNano actual = Json.readValue(json, DataNano.class);
        DataNano expected = new DataNano(ZONED_DATE_TIME_UTC);
        assertThat(actual).isEqualTo(expected);
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, JacksonZonedDateTimeSerializer.with(df));
        return module;
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<String, ZonedDateTime> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<String, ZonedDateTime> map) {
            this.map = map;
        }

        private Data(ZonedDateTime value) {
            this(Collections.singletonMap("zonedDateTime", value));
        }

    }

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class DataInt {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        private ZonedDateTime zonedDateTime;

    }

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class DataFloat {

        @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
        private ZonedDateTime zonedDateTime;

    }

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class DataNano {

        @JsonFormat(with = JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        private ZonedDateTime zonedDateTime;

    }

}