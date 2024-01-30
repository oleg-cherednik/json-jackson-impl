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
import ru.olegcherednik.json.jackson.LocalZoneOffset;

import java.time.ZonedDateTime;
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
public class JacksonZonedDateTimeKeySerializerTest {

    private static final ZonedDateTime ZONED_DATE_TIME =
            ZonedDateTime.parse("2023-12-23T22:20:36.855+03:00[Europe/Moscow]");

    public void shouldUseToStringWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .registerModule(module);

        Data expected = new Data(ZONED_DATE_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-23T22:20:36.855+03:00[Europe/Moscow]\":\"zonedDateTime\"}}");

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

        String json = mapper.writeValueAsString(new Data(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"1703359236.855000000\":\"zonedDateTime\"}}");
    }

    public void shouldSerializeTimestampWhenWriteDateAsTimestamps() throws JsonProcessingException {
        SimpleModule module = createModule(null);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"1703359236855\":\"zonedDateTime\"}}");
    }

    public void shouldUseDateFormatZoneWhenDateFormatHasZone() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                                                .withZone(LocalZoneId.ASIA_SINGAPORE);
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);

        String json = mapper.writeValueAsString(new Data(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-24T03:20:36.855+08:00\":\"zonedDateTime\"}}");

        JsonSettings settings = JsonSettings.builder().zonedDateTimeFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        Data expected = new Data(ZONED_DATE_TIME.withZoneSameInstant(LocalZoneId.ASIA_SINGAPORE));
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldUseContextZoneWhenContextZoneExists() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("'[one]'yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        String json = mapper.writeValueAsString(new Data(ZONED_DATE_TIME));
        assertThat(json).isEqualTo("{\"map\":{\"[one]2023-12-24T03:20:36.855+08:00\":\"zonedDateTime\"}}");

        JsonSettings settings = JsonSettings.builder().zonedDateTimeFormatter(df).build();
        Data actual = Json.createReader(settings).readValue(json, Data.class);
        Data expected = new Data(ZONED_DATE_TIME.toOffsetDateTime()
                                                .withOffsetSameInstant(LocalZoneOffset.ASIA_SINGAPORE)
                                                .toZonedDateTime());
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldIgnoreContextZoneWhenDisableWriteDateWithContextTimeZone() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .setTimeZone(LocalTimeZone.ASIA_SINGAPORE)
                .registerModule(module);

        Data expected = new Data(ZONED_DATE_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-23T22:20:36.855+03:00[Europe/Moscow]\":\"zonedDateTime\"}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldIgnoreContextZoneWhenDisableWriteDateWithContextTimeZone1() throws JsonProcessingException {
        DateTimeFormatter df = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        SimpleModule module = createModule(df);

        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .registerModule(module);

        Data expected = new Data(ZONED_DATE_TIME);
        String json = mapper.writeValueAsString(expected);
        assertThat(json).isEqualTo("{\"map\":{\"2023-12-23T22:20:36.855+03:00[Europe/Moscow]\":\"zonedDateTime\"}}");

        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isEqualTo(expected);
    }

    private static SimpleModule createModule(DateTimeFormatter df) {
        SimpleModule module = new SimpleModule();
        module.addKeySerializer(ZonedDateTime.class, new JacksonZonedDateTimeKeySerializer(df));
        return module;
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<ZonedDateTime, String> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<ZonedDateTime, String> map) {
            this.map = map;
        }

        private Data(ZonedDateTime key) {
            this(Collections.singletonMap(key, "zonedDateTime"));
        }

    }

}
