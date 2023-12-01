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

package ru.olegcherednik.json.jacksonutils;

import org.testng.annotations.Test;
import ru.olegcherednik.jackson_utils.ListUtils;
import ru.olegcherednik.jackson_utils.MapUtils;
import ru.olegcherednik.jackson_utils.data.Book;
import ru.olegcherednik.jackson_utils.data.Data;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonException;

import java.io.IOException;
import java.math.BigInteger;
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

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(Json.readValue((String) null, Object.class)).isNull();
    }

    public void shouldRetrieveEmptyCollectionWhenObjectNull() {
        assertThat(Json.readList((String) null)).isEmpty();
        assertThat(Json.readList((String) null, Object.class)).isEmpty();
        assertThat(Json.readSet((String) null)).isEmpty();
        assertThat(Json.readSet((String) null, Object.class)).isEmpty();
        assertThat(Json.readListOfMap((String) null)).isEmpty();
        assertThat(Json.readMap((String) null)).isEmpty();
        assertThat(Json.readMap((String) null, Object.class)).isEmpty();
        assertThat(Json.readMap((String) null, String.class, String.class)).isEmpty();
    }

    public void shouldRetrieveDeserializedObjectWhenReadValue() throws IOException {
        String json = ResourceData.getResourceAsString("/data.json");
        Data actual = Json.readValue(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyValue() {
        Data actual = Json.readValue("{}", Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data());
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericList() {
        String json = "[1,2.0,3.1,12345678912,123456789123456789123456789123456789]";
        List<Object> actual = Json.readList(json);

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveUniqueValuesWhenReadListNoUniqueValueAsSet() {
        String json = "[\"one\",\"two\",\"three\",\"two\",\"one\"]";
        Set<String> actual = Json.readSet(json, String.class);

        assertThat(actual).hasSize(3);
        assertThat(actual).containsExactly("one", "two", "three");
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericSet() {
        String json = "[1,2.0,3.1,12345678912,123456789123456789123456789123456789]";
        Set<Object> actual = Json.readSet(json);

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveDeserializedListWhenReadAsList() throws IOException {
        String json = ResourceData.getResourceAsString("/data_list.json");
        List<Data> actual = Json.readList(json, Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ListUtils.of(new Data(555, "victory"), new Data(666, "omen")));
    }

    public void shouldRetrieveListOfMapWhenRead() throws IOException {
        String json = ResourceData.getResourceAsString("/data_list.json");
        List<Map<String, Object>> actual = Json.readListOfMap(json);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).hasSize(2);
        assertThat(actual.get(0)).containsEntry("intVal", 555);
        assertThat(actual.get(0)).containsEntry("strVal", "victory");
        assertThat(actual.get(1)).hasSize(2);
        assertThat(actual.get(1)).containsEntry("intVal", 666);
        assertThat(actual.get(1)).containsEntry("strVal", "omen");
    }

    public void shouldRetrieveDataMapWhenReadAsMapWithStringKey() throws IOException {
        String json = ResourceData.getResourceAsString("/variable_value_map.json");
        Map<String, Object> actual = Json.readMap(json);
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("sample", "order");
        assertThat(actual).containsEntry("sample", ListUtils.of("one, two", "three"));
        assertThat(actual).containsEntry("order", MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueMapWhenReadAsMapWithStringKeyAndType() throws IOException {
        String json = ResourceData.getResourceAsString("/string_value_map_s.json");
        Map<String, String> actual = Json.readMap(json, String.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(MapUtils.of("auto", "Audi", "model", "RS3"));
    }

    public void shouldRetrieveDeserializedMapWhenReadAsMapListWithStringKeyAndBookType() throws IOException {
        Map<String, Book> expected = MapUtils.of("one", new Book("Thinking in Java",
                                                                 ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                                                                 1998,
                                                                 ListUtils.of("Bruce Eckel")),
                                                 "two", new Book("Ready for a victory",
                                                                 ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                                                                 2020,
                                                                 ListUtils.of("Oleg Cherednik"))
        );

        String json = ResourceData.getResourceAsString("/books_dict_string_key.json");
        Map<String, Book> actual = Json.readMap(json, Book.class);
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
                        ListUtils.of("Oleg Cherednik")));

        String json = ResourceData.getResourceAsString("/books_dict_int_key.json");
        Map<Integer, Book> actual = Json.readMap(json, Integer.class, Book.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveEmptyListWhenReadEmptyJsonAsList() {
        assertThat(Json.readList("[]")).isEmpty();
        assertThat(Json.readList("[]", Data.class)).isEmpty();
        assertThat(Json.readSet("[]")).isEmpty();
        assertThat(Json.readSet("[]", Data.class)).isEmpty();
        assertThat(Json.readListOfMap("[]")).isEmpty();
        assertThat(Json.readMap("{}")).isEmpty();
        assertThat(Json.readMap("{}", Data.class)).isEmpty();
        assertThat(Json.readMap("{}", String.class, Data.class)).isEmpty();
    }

    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    public void shouldThrowJsonExceptionWhenReadIncorrectJson() {
        assertThatThrownBy(() -> Json.readValue("incorrect", Data.class))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readList("incorrect", Data.class))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readMap("incorrect"))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readMap("incorrect", Data.class))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readMap("incorrect", String.class, Data.class))
                .isExactlyInstanceOf(JsonException.class);
    }

}
