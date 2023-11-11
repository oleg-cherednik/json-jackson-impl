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

package ru.olegcherednik.jackson_utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 13.03.2022
 */
@Test
public class DateJacksonUtilsTest {

    public void shouldRetrieveJsonUTCZoneWhenWriteDateDefaultSettings() throws ParseException {
        Map<String, Date> map = createData();
        String actual = JacksonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                                             "\"Asia/Singapore\":\"2017-07-23T05:57:14.225Z\"," +
                                             "\"Australia/Sydney\":\"2017-07-23T03:57:14.225Z\"}");
    }

    public void shouldRetrieveJsonSingaporeZoneWhenWriteDateSingaporeZone() throws ParseException {
        Supplier<ObjectMapper> mapperSupplier = JacksonObjectMapperSupplier.builder()
                                                                           .zone(LocalZoneId.ASIA_SINGAPORE)
                                                                           .build();
        ObjectMapperDecorator jacksonUtils = JacksonUtilsHelper.createMapperDecorator(mapperSupplier);
        Map<String, Date> map = createData();
        String actual = jacksonUtils.writeValue(map);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00\"," +
                                             "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00\"," +
                                             "\"Australia/Sydney\":\"2017-07-23T11:57:14.225+08:00\"}");
    }

    public void shouldRetrieveJsonWithUTCZoneWhenWriteDateWithSameZone() throws ParseException {
        Supplier<ObjectMapper> mapperSupplier = JacksonObjectMapperSupplier.builder()
                                                                           .zoneModifier(JacksonObjectMapperSupplier.ZONE_MODIFIER_USE_ORIGINAL)
                                                                           .build();
        ObjectMapperDecorator jacksonUtils = JacksonUtilsHelper.createMapperDecorator(mapperSupplier);
        Map<String, Date> map = createData();
        String actual = jacksonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                                             "\"Asia/Singapore\":\"2017-07-23T05:57:14.225Z\"," +
                                             "\"Australia/Sydney\":\"2017-07-23T03:57:14.225Z\"}");
    }

    public void shouldRetrieveDeserializedDateMapWhenReadJsonAsMap() throws ParseException {
        String json = "{\"UTC\":\"2017-07-23T13:57:14.225Z\"," +
                "\"Asia/Singapore\":\"2017-07-23T13:57:14.225+08:00\"," +
                "\"Australia/Sydney\":\"2017-07-23T13:57:14.225+10:00\"}";
        Map<String, Date> expected = createData();
        Map<String, Date> actual = JacksonUtils.readMap(json, String.class, Date.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    static Map<String, Date> createData() throws ParseException {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return MapUtils.of(
                "UTC", Date.from(ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)).toInstant()),
                "Asia/Singapore", Date.from(ZonedDateTime.parse(str, df.withZone(LocalZoneId.ASIA_SINGAPORE)).toInstant()),
                "Australia/Sydney", Date.from(ZonedDateTime.parse(str, df.withZone(LocalZoneId.AUSTRALIA_SYDNEY)).toInstant()));
    }

}
