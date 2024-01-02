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

package ru.olegcherednik.json.jackson.datetime;

import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.jackson.LocalZoneId;
import ru.olegcherednik.json.jackson.MapUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.01.2021
 */
@Test
public class ZonedDateTimeTest {

    public void shouldRetrieveJsonOriginalZoneWhenWriteDefaultSettings() {
        String actual = Json.createWriter().writeValue(createData());
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(
                "{\"UTC\":\"2017-07-23T13:57:14.225Z\","
                        + "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\","
                        + "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}");
    }

    public void shouldRetrieveJsonUtcZoneWhenWriteZonedDateTimeWithUtcZoneId() {
        JsonSettings settings = JsonSettings.builder()
                                            .zoneId(ZoneOffset.UTC)
                                            .build();

        String actual = Json.createWriter(settings).writeValue(createData());
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\","
                                             + "\"Asia/Singapore\":\"2017-07-23T05:57:14.225Z\","
                                             + "\"Australia/Sydney\":\"2017-07-23T03:57:14.225Z\"}");
    }

    public void shouldRetrieveJsonSingaporeWhenWriteZonedDateTimeWithSingaporeZoneId() {
        JsonSettings settings = JsonSettings.builder()
                                            .zoneId(LocalZoneId.ASIA_SINGAPORE)
                                            .build();

        String actual = Json.createWriter(settings).writeValue(createData());
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\","
                                             + "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\","
                                             + "\"Australia/Sydney\":\"2017-07-23T11:57:14.225+08:00[Asia/Singapore]\"}");
    }

    public void shouldRetrieveDeserializedZonedDateTimeMapWhenReadJsonAsMap() {
        String json = "{\"UTC\":\"2017-07-23T13:57:14.225Z\","
                + "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\","
                + "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00[Australia/Sydney]\"}";

        Map<String, ZonedDateTime> actual = Json.readMap(json, String.class, ZonedDateTime.class);
        Map<String, ZonedDateTime> expected = createData();
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return MapUtils.of("UTC", ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)),
                           "Asia/Singapore", ZonedDateTime.parse(str, df.withZone(LocalZoneId.ASIA_SINGAPORE)),
                           "Australia/Sydney", ZonedDateTime.parse(str, df.withZone(LocalZoneId.AUSTRALIA_SYDNEY)));
    }

}
