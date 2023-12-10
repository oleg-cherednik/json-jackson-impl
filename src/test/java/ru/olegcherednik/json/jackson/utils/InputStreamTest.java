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

package ru.olegcherednik.json.jackson.utils;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;
import ru.olegcherednik.json.jackson.utils.data.Book;
import ru.olegcherednik.json.jackson.utils.data.Data;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Oleg Cherednik
 * @since 03.01.2021
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class InputStreamTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(Json.readValue((InputStream) null, Object.class)).isNull();
        assertThat(Json.readListLazy((InputStream) null)).isNull();
        assertThat(Json.readListLazy((InputStream) null, Object.class)).isNull();
        assertThat(Json.readListOfMapLazy((InputStream) null)).isNull();
    }

    public void shouldRetrieveEmptyCollectionWhenObjectNull() {
        assertThat(Json.readList((InputStream) null)).isEmpty();
        assertThat(Json.readList((InputStream) null, Object.class)).isEmpty();
        assertThat(Json.readSet((InputStream) null)).isEmpty();
        assertThat(Json.readSet((InputStream) null, Object.class)).isEmpty();
        assertThat(Json.readListOfMap((InputStream) null)).isEmpty();
        assertThat(Json.readMap((InputStream) null)).isEmpty();
        assertThat(Json.readMap((InputStream) null, String.class)).isEmpty();
        assertThat(Json.readMap((InputStream) null, String.class, String.class)).isEmpty();
    }

    public void shouldRetrieveDeserializedObjectWhenReadValue() throws IOException {
        Data actual = Json.readValue(getResourceAsInputStream("/data.json"), Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyValue() throws IOException {
        Data actual = Json.readValue(convertToInputStream("{}"), Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data());
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericList() throws IOException {
        List<Object> actual = Json.readList(
                convertToInputStream("[1,2.0,3.1,12345678912,123456789123456789123456789123456789]"));

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveUniqueValuesWhenReadListNoUniqueValueAsSet() throws IOException {
        Set<Object> actual = Json.readSet(convertToInputStream("[\"one\",\"two\",\"three\",\"two\",\"one\"]"));

        assertThat(actual).hasSize(3);
        assertThat(actual).containsExactly("one", "two", "three");
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericSet() throws IOException {
        Set<Object> actual = Json.readSet(
                convertToInputStream("[1,2.0,3.1,12345678912,123456789123456789123456789123456789]"));

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveDeserializedListWhenReadAsList() throws IOException {
        List<Data> actual = Json.readList(getResourceAsInputStream("/data_list.json"), Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ListUtils.of(new Data(555, "victory"), new Data(666, "omen")));
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadAsLazyList() throws IOException {
        Map<String, Object> expected1 = MapUtils.of("title", "Thinking in Java",
                                                    "date", "2017-07-23T13:57:14.225Z",
                                                    "year", 1998,
                                                    "authors", ListUtils.of("Bruce Eckel"));
        Map<String, Object> expected2 = MapUtils.of("title", "Ready for a victory",
                                                    "date", "2020-07-23T13:57:14.225Z",
                                                    "year", 2020,
                                                    "authors", ListUtils.of("Oleg Cherednik"));

        Iterator<Object> it = Json.readListLazy(getResourceAsInputStream("/books.json"));
        assertThat(it.hasNext()).isTrue();

        Object actual1 = it.next();
        assertThat(actual1).isNotNull();
        assertThat(actual1).isEqualTo(expected1);
        assertThat(it.hasNext()).isTrue();

        Object actual2 = it.next();
        assertThat(actual2).isNotNull();
        assertThat(actual2).isEqualTo(expected2);
        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadValueLazyList() throws IOException {
        Book expected1 = new Book("Thinking in Java",
                                  ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                                  1998,
                                  ListUtils.of("Bruce Eckel"));
        Book expected2 = new Book("Ready for a victory",
                                  ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                                  2020,
                                  ListUtils.of("Oleg Cherednik"));

        Iterator<Book> it = Json.readListLazy(getResourceAsInputStream("/books.json"), Book.class);
        assertThat(it.hasNext()).isTrue();

        Book actual1 = it.next();
        assertThat(actual1).isNotNull();
        assertThat(actual1).isEqualTo(expected1);
        assertThat(it.hasNext()).isTrue();

        Book actual2 = it.next();
        assertThat(actual2).isNotNull();
        assertThat(actual2).isEqualTo(expected2);
        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveListOfMapWhenRead() throws IOException {
        List<Map<String, Object>> actual = Json.readListOfMap(getResourceAsInputStream("/data_list.json"));

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).hasSize(2);
        assertThat(actual.get(0)).containsEntry("intVal", 555);
        assertThat(actual.get(0)).containsEntry("strVal", "victory");
        assertThat(actual.get(1)).hasSize(2);
        assertThat(actual.get(1)).containsEntry("intVal", 666);
        assertThat(actual.get(1)).containsEntry("strVal", "omen");
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadByteBufferAsListOfMapLazy() throws IOException {
        Map<String, Object> expected1 = MapUtils.of("title", "Thinking in Java",
                                                    "date", "2017-07-23T13:57:14.225Z",
                                                    "year", 1998,
                                                    "authors", ListUtils.of("Bruce Eckel"));
        Map<String, Object> expected2 = MapUtils.of("title", "Ready for a victory",
                                                    "date", "2020-07-23T13:57:14.225Z",
                                                    "year", 2020,
                                                    "authors", ListUtils.of("Oleg Cherednik"));

        Iterator<Map<String, Object>> it = Json.readListOfMapLazy(getResourceAsInputStream("/books.json"));
        assertThat(it.hasNext()).isTrue();

        Object actual1 = it.next();
        assertThat(actual1).isNotNull();
        assertThat(actual1).isEqualTo(expected1);
        assertThat(it.hasNext()).isTrue();

        Object actual2 = it.next();
        assertThat(actual2).isNotNull();
        assertThat(actual2).isEqualTo(expected2);
        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveDataMapWhenReadAsMapWithStringKey() throws IOException {
        Map<String, Object> actual = Json.readMap(getResourceAsInputStream("/variable_value_map.json"));
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("sample", "order");
        assertThat(actual).containsEntry("sample", ListUtils.of("one, two", "three"));
        assertThat(actual).containsEntry("order", MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueMapWhenReadAsMapWithStringKeyAndType() throws IOException {
        Map<String, String> actual = Json.readMap(
                getResourceAsInputStream("/string_value_map_s.json"), String.class);
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

        Map<String, Book> actual = Json.readMap(
                getResourceAsInputStream("/books_dict_string_key.json"), Book.class);
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

        Map<Integer, Book> actual = Json.readMap(getResourceAsInputStream("/books_dict_int_key.json"),
                                                 Integer.class, Book.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveEmptyListWhenReadEmptyByteBufferAsList() {
        assertThat(Json.readList(convertToInputStream("[]"))).isEmpty();
        assertThat(Json.readList(convertToInputStream("[]"), Data.class)).isEmpty();
        assertThat(Json.readSet(convertToInputStream("[]"))).isEmpty();
        assertThat(Json.readSet(convertToInputStream("[]"), Data.class)).isEmpty();
        assertThat(Json.readListOfMap(convertToInputStream("[]"))).isEmpty();
        assertThat(Json.readMap(convertToInputStream("{}"))).isEmpty();
        assertThat(Json.readMap(convertToInputStream("{}"), Data.class)).isEmpty();
        assertThat(Json.readMap(convertToInputStream("{}"), String.class, Data.class)).isEmpty();
    }

    public void shouldThrowJsonExceptionWhenReadIncorrectByteBuffer() {
        assertThatThrownBy(() -> Json.readValue(convertToInputStream("incorrect"), Data.class))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readMap(convertToInputStream("incorrect")))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readMap(convertToInputStream("incorrect"), Data.class))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readMap(convertToInputStream("incorrect"), String.class, Data.class))
                .isExactlyInstanceOf(JsonException.class);
    }

    private static InputStream getResourceAsInputStream(String name) throws IOException {
        return InputStreamTest.class.getResourceAsStream(name);
    }

    private static InputStream convertToInputStream(String str) {
        return IOUtils.toInputStream(str, StandardCharsets.UTF_8);
    }

}


