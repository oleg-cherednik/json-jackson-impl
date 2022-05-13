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

import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.olegcherednik.jackson.utils.data.Book;
import ru.olegcherednik.jackson.utils.data.Data;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@Test
public class StringJacksonUtilsTest {

    @BeforeClass
    public static void init() {
        try {
            Constructor<JacksonUtils> constructor = JacksonUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch(Exception ignored) {
        }
    }

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(JacksonUtils.readValue((String)null, Object.class)).isNull();
        assertThat(JacksonUtils.readList((String)null)).isNull();
        assertThat(JacksonUtils.readList((String)null, Object.class)).isNull();
        assertThat(JacksonUtils.readSet((String)null)).isNull();
        assertThat(JacksonUtils.readSet((String)null, Object.class)).isNull();
        assertThat(JacksonUtils.readListOfMap((String)null)).isNull();
        assertThat(JacksonUtils.readMap((String)null)).isNull();
        assertThat(JacksonUtils.readMap((String)null, Object.class)).isNull();
        assertThat(JacksonUtils.readMap((String)null, String.class, String.class)).isNull();
    }

    public void shouldRetrieveDeserializedObjectWhenReadValue() throws IOException {
        String json = getResourceAsString("/data.json");
        Data actual = JacksonUtils.readValue(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyValue() {
        Data actual = JacksonUtils.readValue("{}", Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data());
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericList() {
        String json = "[1,2.0,3.1,12345678912,123456789123456789123456789123456789]";
        List<Object> actual = JacksonUtils.readList(json);

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveUniqueValuesWhenReadListNoUniqueValueAsSet() {
        String json = "[\"one\",\"two\",\"three\",\"two\",\"one\"]";
        Set<String> actual = JacksonUtils.readSet(json, String.class);

        assertThat(actual).hasSize(3);
        assertThat(actual).containsExactly("one", "two", "three");
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericSet() {
        String json = "[1,2.0,3.1,12345678912,123456789123456789123456789123456789]";
        Set<Object> actual = JacksonUtils.readSet(json);

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveDeserializedListWhenReadAsList() throws IOException {
        String json = getResourceAsString("/data_list.json");
        List<Data> actual = JacksonUtils.readList(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ListUtils.of(new Data(555, "victory"), new Data(666, "omen")));
    }

    public void shouldRetrieveListOfMapWhenRead() throws IOException {
        String json = getResourceAsString("/data_list.json");
        List<Map<String, Object>> actual = JacksonUtils.readListOfMap(json);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).hasSize(2);
        assertThat(actual.get(0)).containsEntry("intVal", 555);
        assertThat(actual.get(0)).containsEntry("strVal", "victory");
        assertThat(actual.get(1)).hasSize(2);
        assertThat(actual.get(1)).containsEntry("intVal", 666);
        assertThat(actual.get(1)).containsEntry("strVal", "omen");
    }

    public void shouldRetrieveDataMapWhenReadAsMapWithStringKey() throws IOException {
        String json = getResourceAsString("/variable_value_map.json");
        Map<String, Object> actual = JacksonUtils.readMap(json);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("sample", "order");
        assertThat(actual).containsEntry("sample", ListUtils.of("one, two", "three"));
        assertThat(actual).containsEntry("order", MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueMapWhenReadAsMapWithStringKeyAndType() throws IOException {
        String json = getResourceAsString("/string_value_map_s.json");
        Map<String, String> actual = JacksonUtils.readMap(json, String.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(MapUtils.of("auto", "Audi", "model", "RS3"));
    }

    public void shouldRetrieveDeserializedMapWhenReadAsMapListWithStringKeyAndBookType() throws IOException {
        Map<String, Book> expected = MapUtils.of(
                "one", new Book(
                        "Thinking in Java",
                        ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                        1998,
                        ListUtils.of("Bruce Eckel")),
                "two", new Book(
                        "Ready for a victory",
                        ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                        2020,
                        ListUtils.of("Oleg Cherednik"))
        );

        String json = getResourceAsString("/books_dict_string_key.json");
        Map<String, Book> actual = JacksonUtils.readMap(json, Book.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveIntegerValueMapWhenReadAsMapWithIntKeyAndBookType() throws IOException {
        Map<Integer, Book> expected = MapUtils.of(
                1, new Book(
                        "Thinking in Java",
                        ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                        1998,
                        ListUtils.of("Bruce Eckel")),
                2, new Book(
                        "Ready for a victory",
                        ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                        2020,
                        ListUtils.of("Oleg Cherednik"))
        );

        String json = getResourceAsString("/books_dict_int_key.json");
        Map<Integer, Book> actual = JacksonUtils.readMap(json, Integer.class, Book.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveEmptyListWhenReadEmptyJsonAsList() {
        assertThat(JacksonUtils.readList("[]")).isEmpty();
        assertThat(JacksonUtils.readList("[]", Data.class)).isEmpty();
        assertThat(JacksonUtils.readSet("[]")).isEmpty();
        assertThat(JacksonUtils.readSet("[]", Data.class)).isEmpty();
        assertThat(JacksonUtils.readListOfMap("[]")).isEmpty();
        assertThat(JacksonUtils.readMap("{}")).isEmpty();
        assertThat(JacksonUtils.readMap("{}", Data.class)).isEmpty();
        assertThat(JacksonUtils.readMap("{}", String.class, Data.class)).isEmpty();
    }

    public void shouldThrowJacksonUtilsExceptionWhenReadIncorrectJson() {
        assertThatThrownBy(() -> JacksonUtils.readValue("incorrect", Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readList("incorrect", Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap("incorrect"))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap("incorrect", Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap("incorrect", String.class, Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
    }

    private static String getResourceAsString(String name) throws IOException {
        try (InputStream in = StringJacksonUtilsTest.class.getResourceAsStream(name)) {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        }
    }

}
