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
import ru.olegcherednik.jackson_utils.data.Book;
import ru.olegcherednik.jackson_utils.data.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Oleg Cherednik
 * @since 19.11.2023
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class ReaderJacksonUtilsTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(JacksonUtils.readValue((Reader) null, Object.class)).isNull();
        assertThat(JacksonUtils.readListLazy((Reader) null)).isNull();
        assertThat(JacksonUtils.readListLazy((Reader) null, Object.class)).isNull();
        assertThat(JacksonUtils.readListOfMapLazy((Reader) null)).isNull();
    }

    public void shouldRetrieveEmptyCollectionWhenObjectNull() {
        assertThat(JacksonUtils.readList((Reader) null)).isEmpty();
        assertThat(JacksonUtils.readList((Reader) null, Object.class)).isEmpty();
        assertThat(JacksonUtils.readSet((Reader) null)).isEmpty();
        assertThat(JacksonUtils.readSet((Reader) null, Object.class)).isEmpty();
        assertThat(JacksonUtils.readListOfMap((Reader) null)).isEmpty();
        assertThat(JacksonUtils.readMap((Reader) null)).isEmpty();
        assertThat(JacksonUtils.readMap((Reader) null, String.class)).isEmpty();
        assertThat(JacksonUtils.readMap((Reader) null, String.class, String.class)).isEmpty();
    }

    public void shouldRetrieveDeserializedObjectWhenReadValue() throws IOException {
        Data actual = JacksonUtils.readValue(getResourceAsReader("/data.json"), Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyValue() throws IOException {
        Data actual = JacksonUtils.readValue(convertToReader("{}"), Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(new Data());
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericList() throws IOException {
        List<Object> actual = JacksonUtils.readList(
                convertToReader("[1,2.0,3.1,12345678912,123456789123456789123456789123456789]"));

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveUniqueValuesWhenReadListNoUniqueValueAsSet() throws IOException {
        Set<Object> actual = JacksonUtils.readSet(convertToReader("[\"one\",\"two\",\"three\",\"two\",\"one\"]"));

        assertThat(actual).hasSize(3);
        assertThat(actual).containsExactly("one", "two", "three");
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericSet() throws IOException {
        Set<Object> actual = JacksonUtils.readSet(
                convertToReader("[1,2.0,3.1,12345678912,123456789123456789123456789123456789]"));

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveDeserializedListWhenReadAsList() throws IOException {
        List<Data> actual = JacksonUtils.readList(getResourceAsReader("/data_list.json"), Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ListUtils.of(new Data(555, "victory"), new Data(666, "omen")));
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadAsLazyList() throws IOException {
        Map<String, Object> expected1 = MapUtils.of(
                "title", "Thinking in Java",
                "date", "2017-07-23T13:57:14.225Z",
                "year", 1998,
                "authors", ListUtils.of("Bruce Eckel"));
        Map<String, Object> expected2 = MapUtils.of(
                "title", "Ready for a victory",
                "date", "2020-07-23T13:57:14.225Z",
                "year", 2020,
                "authors", ListUtils.of("Oleg Cherednik"));

        Iterator<Object> it = JacksonUtils.readListLazy(getResourceAsReader("/books.json"));
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
        Book expected1 = new Book(
                "Thinking in Java",
                ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                1998,
                ListUtils.of("Bruce Eckel"));
        Book expected2 = new Book(
                "Ready for a victory",
                ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                2020,
                ListUtils.of("Oleg Cherednik"));

        Iterator<Book> it = JacksonUtils.readListLazy(getResourceAsReader("/books.json"), Book.class);
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
        List<Map<String, Object>> actual = JacksonUtils.readListOfMap(getResourceAsReader("/data_list.json"));

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).hasSize(2);
        assertThat(actual.get(0)).containsEntry("intVal", 555);
        assertThat(actual.get(0)).containsEntry("strVal", "victory");
        assertThat(actual.get(1)).hasSize(2);
        assertThat(actual.get(1)).containsEntry("intVal", 666);
        assertThat(actual.get(1)).containsEntry("strVal", "omen");
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadByteBufferAsListOfMapLazy() throws IOException {
        Map<String, Object> expected1 = MapUtils.of(
                "title", "Thinking in Java",
                "date", "2017-07-23T13:57:14.225Z",
                "year", 1998,
                "authors", ListUtils.of("Bruce Eckel"));
        Map<String, Object> expected2 = MapUtils.of(
                "title", "Ready for a victory",
                "date", "2020-07-23T13:57:14.225Z",
                "year", 2020,
                "authors", ListUtils.of("Oleg Cherednik"));

        Iterator<Map<String, Object>> it = JacksonUtils.readListOfMapLazy(getResourceAsReader("/books.json"));
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
        Map<String, Object> actual = JacksonUtils.readMap(getResourceAsReader("/variable_value_map.json"));
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("sample", "order");
        assertThat(actual).containsEntry("sample", ListUtils.of("one, two", "three"));
        assertThat(actual).containsEntry("order", MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueMapWhenReadAsMapWithStringKeyAndType() throws IOException {
        Map<String, String> actual = JacksonUtils.readMap(
                getResourceAsReader("/string_value_map_s.json"), String.class);
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

        Map<String, Book> actual = JacksonUtils.readMap(
                getResourceAsReader("/books_dict_string_key.json"), Book.class);
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

        Map<Integer, Book> actual = JacksonUtils.readMap(getResourceAsReader("/books_dict_int_key.json"),
                                                         Integer.class, Book.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveEmptyListWhenReadEmptyByteBufferAsList() {
        assertThat(JacksonUtils.readList(convertToReader("[]"))).isEmpty();
        assertThat(JacksonUtils.readList(convertToReader("[]"), Data.class)).isEmpty();
        assertThat(JacksonUtils.readSet(convertToReader("[]"))).isEmpty();
        assertThat(JacksonUtils.readSet(convertToReader("[]"), Data.class)).isEmpty();
        assertThat(JacksonUtils.readListOfMap(convertToReader("[]"))).isEmpty();
        assertThat(JacksonUtils.readMap(convertToReader("{}"))).isEmpty();
        assertThat(JacksonUtils.readMap(convertToReader("{}"), Data.class)).isEmpty();
        assertThat(JacksonUtils.readMap(convertToReader("{}"), String.class, Data.class)).isEmpty();
    }

    public void shouldThrowJacksonUtilsExceptionWhenReadIncorrectByteBuffer() {
        assertThatThrownBy(() -> JacksonUtils.readValue(convertToReader("incorrect"), Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap(convertToReader("incorrect")))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap(convertToReader("incorrect"), Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
        assertThatThrownBy(() -> JacksonUtils.readMap(convertToReader("incorrect"), String.class, Data.class))
                .isExactlyInstanceOf(JacksonUtilsException.class);
    }

    private static Reader getResourceAsReader(String name) throws IOException {
        InputStream in = Objects.requireNonNull(ReaderJacksonUtilsTest.class.getResourceAsStream(name));
        return new InputStreamReader(in);
    }

    private static Reader convertToReader(String str) {
        return new StringReader(str);
    }

}


