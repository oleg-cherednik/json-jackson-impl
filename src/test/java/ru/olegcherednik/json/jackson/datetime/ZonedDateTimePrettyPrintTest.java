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
import ru.olegcherednik.json.api.ZoneModifier;
import ru.olegcherednik.json.jackson.LocalZoneId;
import ru.olegcherednik.json.jackson.ResourceData;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@Test
public class ZonedDateTimePrettyPrintTest {

//    public void shouldRetrievePrettyPrintJsonUtcZoneWhenWriteZonedDateTimeMapWithPrettyPrint() throws IOException {
//        JsonSettings settings = JsonSettings.builder()
//                                            .zoneModifier(ZoneModifier.CONVERT_TO_UTC)
//                                            .build();
//
//        Map<String, ZonedDateTime> map = ZonedDateTimeTest.createData();
//        String actual = Json.createPrettyPrint(settings).writeValue(map);
//        String expected = ResourceData.getResourceAsString("/datetime/zoned_date_time_utc.json").trim();
//
//        assertThat(actual).isNotEqualTo(expected);
//        assertThat(Json.readMap(actual)).isEqualTo(Json.readMap(expected));
//    }

//    public void shouldRetrievePrettyPrintJsonSingaporeZoneWhenWriteZonedDateTimeMapWithPrettyPrint()
//            throws IOException {
//        JsonSettings settings = JsonSettings.builder()
//                                            .zoneModifier(zone -> LocalZoneId.ASIA_SINGAPORE)
//                                            .build();
//
//        Map<String, ZonedDateTime> map = ZonedDateTimeTest.createData();
//        String actual = Json.createPrettyPrint(settings).writeValue(map);
//        String expected = ResourceData.getResourceAsString("/datetime/zoned_date_time_singapore.json").trim();
//
//        assertThat(actual).isNotEqualTo(expected);
//        assertThat(Json.readMap(actual)).isEqualTo(Json.readMap(expected));
//    }

}
