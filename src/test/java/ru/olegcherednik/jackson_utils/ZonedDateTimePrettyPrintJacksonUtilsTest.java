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

import org.testng.annotations.Test;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@SuppressWarnings("NewClassNamingConvention")
@Test
public class ZonedDateTimePrettyPrintJacksonUtilsTest {

    @SuppressWarnings("AbbreviationAsWordInName")
    public void shouldRetrievePrettyPrintJsonUTCZoneWhenWriteZonedDateTimeMapWithPrettyPrint() {
//        Map<String, ZonedDateTime> map = ZonedDateTimeJacksonUtilsTest.createData();
//        String actual = JacksonUtils.prettyPrint().writeValue(map);
//        assertThat(actual)
//                .isEqualTo('{' + LINE_SEPARATOR
//                                   + "  \"UTC\" : \"2017-07-23T13:57:14.225Z\"," + LINE_SEPARATOR
//                                   + "  \"Asia/Singapore\" : \"2017-07-23T05:57:14.225Z\"," + LINE_SEPARATOR
//                                   + "  \"Australia/Sydney\" : \"2017-07-23T03:57:14.225Z\"" + LINE_SEPARATOR
//                                   + '}');
    }

    public void shouldRetrievePrettyPrintJsonSingaporeZoneWhenWriteZonedDateTimeMapWithPrettyPrint() {
//        Supplier<ObjectMapper> mapperSupplier = JacksonObjectMapperSupplier.builder()
//                                                                           .zone(LocalZoneId.ASIA_SINGAPORE)
//                                                                           .build();
//        ObjectMapperDecorator jacksonUtils = JacksonUtilsHelper.createPrettyPrintMapperDecorator(mapperSupplier);
//        Map<String, ZonedDateTime> map = ZonedDateTimeJacksonUtilsTest.createData();
//        String actual = jacksonUtils.writeValue(map);
//        assertThat(actual).isEqualTo(
//                '{' + LINE_SEPARATOR
//                        + "  \"UTC\" : \"2017-07-23T21:57:14.225+08:00\"," + LINE_SEPARATOR
//                        + "  \"Asia/Singapore\" : \"2017-07-23T13:57:14.225+08:00\"," + LINE_SEPARATOR
//                        + "  \"Australia/Sydney\" : \"2017-07-23T11:57:14.225+08:00\"" + LINE_SEPARATOR
//                        + '}');
    }

}
