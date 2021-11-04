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

import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 01.01.2021
 */
@Test
public class LocalDateTimeJacksonUtilsTest {

    public void shouldRetrieveJsonWhenWriteZonedDateTime() throws IOException {
        Map<String, LocalDateTime> map = createData();
        String actual = JacksonUtils.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"local\":\"2017-07-23T13:57:14.225\"}");
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteZonedDateTimeMapWithPrettyPrint() {
        Map<String, LocalDateTime> map = createData();
        String actual = JacksonUtils.prettyPrint().writeValue(map);
        assertThat(actual).isEqualTo('{' + System.lineSeparator() +
                "  \"local\" : \"2017-07-23T13:57:14.225\"" + System.lineSeparator() +
                '}');
    }

    public void shouldRetrieveDeserializedZonedDateTimeMapWhenReadJsonAsMap() {
        String json = "{\"local\":\"2017-07-23T13:57:14.225\"}";
        Map<String, LocalDateTime> expected = createData();
        Map<String, LocalDateTime> actual = JacksonUtils.readMap(json, String.class, LocalDateTime.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    private static Map<String, LocalDateTime> createData() {
        String str = "2017-07-23T13:57:14.225";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return MapUtils.of("local", LocalDateTime.parse(str, df));
    }

}
