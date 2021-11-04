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
package ru.olegcherednik.utils.jackson;

import org.testng.annotations.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@Test
public class ZonedDateTimePrettyPrintJacksonUtilsTest {

    public void shouldRetrievePrettyPrintJsonUTCZoneWhenWriteZonedDateTimeMapWithPrettyPrint() {
        Map<String, ZonedDateTime> map = ZonedDateTimeJacksonUtilsTest.createData();
        String actual = JacksonUtils.prettyPrint().writeValue(map);
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"UTC\" : \"2017-07-23T13:57:14.225Z\"," + System.lineSeparator() +
                "  \"Asia/Singapore\" : \"2017-07-23T05:57:14.225Z\"," + System.lineSeparator() +
                "  \"Australia/Sydney\" : \"2017-07-23T03:57:14.225Z\"" + System.lineSeparator() +
                '}');
    }

    public void shouldRetrievePrettyPrintJsonSingaporeZoneWhenWriteZonedDateTimeMapWithPrettyPrint() {
        ObjectMapperDecorator jsonUtils = JacksonHelper.createPrettyPrintMapperDecorator(
                () -> new JacksonObjectMapperBuilder(ZoneId.of("Asia/Singapore")).get());

        Map<String, ZonedDateTime> map = ZonedDateTimeJacksonUtilsTest.createData();
        String actual = jsonUtils.writeValue(map);
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"UTC\" : \"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"," + System.lineSeparator() +
                "  \"Asia/Singapore\" : \"2017-07-23T13:57:14.225+08:00[Asia/Singapore]\"," + System.lineSeparator() +
                "  \"Australia/Sydney\" : \"2017-07-23T11:57:14.225+08:00[Asia/Singapore]\"" + System.lineSeparator() +
                '}');
    }

}
