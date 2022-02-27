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
package ru.olegcherednik.jackson.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 02.01.2021
 */
@Test
public class JacksonHelperTest {

    @AfterMethod
    public void clear() {
        JacksonHelper.setMapperBuilder(null);
    }

    public void shouldUseNewBuilderWhenSetNotNullBuilderToJacksonHelper() {
        Map<String, ZonedDateTime> map = createData();
        assertThat(JacksonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T13:57:14.225Z\"}");
        assertThat(JacksonUtils.prettyPrint().writeValue(map)).isEqualTo('{' + System.lineSeparator() +
                "  \"UTC\" : \"2017-07-23T13:57:14.225Z\"" + System.lineSeparator() +
                '}');

        JacksonHelper.setMapperBuilder(() -> new JacksonObjectMapperBuilder(ZoneId.of("Asia/Singapore")).get());
        assertThat(JacksonUtils.writeValue(map)).isEqualTo("{\"UTC\":\"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"}");
        assertThat(JacksonUtils.prettyPrint().writeValue(map)).isEqualTo('{' + System.lineSeparator() +
                "  \"UTC\" : \"2017-07-23T21:57:14.225+08:00[Asia/Singapore]\"" + System.lineSeparator() +
                '}');
    }

    public void shouldNotRebuildMapperWhenSetSameBuilder() {
        ObjectMapper expectedMapper = JacksonHelper.mapper();
        ObjectMapper expectedPrettyPrintMapper = JacksonHelper.prettyPrintMapper();

        JacksonHelper.setMapperBuilder(JacksonHelper.DEFAULT_BUILDER);
        assertThat(JacksonHelper.mapper()).isSameAs(expectedMapper);
        assertThat(JacksonHelper.prettyPrintMapper()).isSameAs(expectedPrettyPrintMapper);
    }

    private static Map<String, ZonedDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return MapUtils.of("UTC", ZonedDateTime.parse(str, df.withZone(ZoneOffset.UTC)));
    }
}
