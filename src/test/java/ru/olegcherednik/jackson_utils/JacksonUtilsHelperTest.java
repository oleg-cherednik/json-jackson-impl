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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.olegcherednik.jackson_utils.PrettyPrintUtils.LINE_SEPARATOR;

/**
 * @author Oleg Cherednik
 * @since 02.01.2021
 */
@Test
public class JacksonUtilsHelperTest {

    @AfterMethod
    public void clear() {
        JacksonUtilsHelper.setMapperBuilder(null);
    }

    public void shouldUseNewBuilderWhenSetNotNullBuilderToJacksonHelper() {
        Map<String, ZonedDateTime> map = createData();
        assertThat(JacksonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"}");
        assertThat(JacksonUtils.prettyPrint().writeValue(map))
                .isEqualTo('{' + LINE_SEPARATOR
                                   + "  \"UTC\" : \"2017-07-23T13:57:14.225Z\"" + LINE_SEPARATOR
                                   + '}');

        JacksonUtilsHelper.setMapperBuilder(JacksonObjectMapperSupplier.builder()
                                                                       .zone(LocalZoneId.ASIA_SINGAPORE)
                                                                       .build());
        assertThat(JacksonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00\"}");
        assertThat(JacksonUtils.prettyPrint().writeValue(map))
                .isEqualTo('{' + LINE_SEPARATOR
                                   + "  \"UTC\" : \"2017-07-23T21:57:14.225+08:00\"" + LINE_SEPARATOR
                                   + '}');
    }

    public void shouldNotRebuildMapperWhenSetSameBuilder() {
        ObjectMapper expectedMapper = JacksonUtilsHelper.mapper();
        ObjectMapper expectedPrettyPrintMapper = JacksonUtilsHelper.prettyPrintMapper();

        JacksonUtilsHelper.setMapperBuilder(JacksonUtilsHelper.DEFAULT_BUILDER);
        assertThat(JacksonUtilsHelper.mapper()).isSameAs(expectedMapper);
        assertThat(JacksonUtilsHelper.prettyPrintMapper()).isSameAs(expectedPrettyPrintMapper);
    }

    private static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return MapUtils.of("UTC", ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)));
    }

}
