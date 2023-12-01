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

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Oleg Cherednik
 * @since 19.02.2022
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class ByteBufferJacksonUtilsTest {

    public void shouldRetrieveNullWhenObjectNull() {
//        assertThat(JacksonUtils.readValue((ByteBuffer) null, Object.class)).isNull();
//        assertThat(JacksonUtils.readListLazy((ByteBuffer) null)).isNull();
//        assertThat(JacksonUtils.readListLazy((ByteBuffer) null, Object.class)).isNull();
//        assertThat(JacksonUtils.readListOfMapLazy((ByteBuffer) null)).isNull();
    }

    public void shouldRetrieveEmptyCollectionWhenObjectNull() {
//        assertThat(JacksonUtils.readList((ByteBuffer) null)).isEmpty();
//        assertThat(JacksonUtils.readList((ByteBuffer) null, Object.class)).isEmpty();
//        assertThat(JacksonUtils.readSet((ByteBuffer) null)).isEmpty();
//        assertThat(JacksonUtils.readSet((ByteBuffer) null, Object.class)).isEmpty();
//        assertThat(JacksonUtils.readListOfMap((ByteBuffer) null)).isEmpty();
//        assertThat(JacksonUtils.readMap((ByteBuffer) null)).isEmpty();
//        assertThat(JacksonUtils.readMap((ByteBuffer) null, String.class)).isEmpty();
//        assertThat(JacksonUtils.readMap((ByteBuffer) null, String.class, String.class)).isEmpty();
    }

    public void shouldRetrieveDeserializedObjectWhenReadValue() throws IOException {
//        ByteBuffer buf = getResourceAsByteBuffer("/data.json");
//        Data actual = JacksonUtils.readValue(buf, Data.class);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo(new Data(666, "omen"));
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyValue() {
//        ByteBuffer buf = convertToByteBuffer("{}");
//        Data actual = JacksonUtils.readValue(buf, Data.class);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo(new Data());
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericList() {
//        ByteBuffer buf = convertToByteBuffer("[1,2.0,3.1,12345678912,123456789123456789123456789123456789]");
//        List<Object> actual = JacksonUtils.readList(buf);
//
//        assertThat(actual).hasSize(5);
//        assertThat(actual).containsExactly(1,
//                                           2.0,
//                                           3.1,
//                                           12345678912L,
//                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveUniqueValuesWhenReadListNoUniqueValueAsSet() {
//        ByteBuffer buf = convertToByteBuffer("[\"one\",\"two\",\"three\",\"two\",\"one\"]");
//        Set<String> actual = JacksonUtils.readSet(buf, String.class);
//
//        assertThat(actual).hasSize(3);
//        assertThat(actual).containsExactly("one", "two", "three");
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericSet() {
//        ByteBuffer buf = convertToByteBuffer("[1,2.0,3.1,12345678912,123456789123456789123456789123456789]");
//        Set<Object> actual = JacksonUtils.readSet(buf);
//
//        assertThat(actual).hasSize(5);
//        assertThat(actual).containsExactly(1,
//                                           2.0,
//                                           3.1,
//                                           12345678912L,
//                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveDeserializedListWhenReadAsList() throws IOException {
//        ByteBuffer buf = getResourceAsByteBuffer("/data_list.json");
//        List<Data> actual = JacksonUtils.readList(buf, Data.class);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo(ListUtils.of(new Data(555, "victory"), new Data(666, "omen")));
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadAsLazyList() throws IOException {
//        Map<String, Object> expected1 = MapUtils.of(
//                "title", "Thinking in Java",
//                "date", "2017-07-23T13:57:14.225Z",
//                "year", 1998,
//                "authors", ListUtils.of("Bruce Eckel"));
//        Map<String, Object> expected2 = MapUtils.of(
//                "title", "Ready for a victory",
//                "date", "2020-07-23T13:57:14.225Z",
//                "year", 2020,
//                "authors", ListUtils.of("Oleg Cherednik"));
//
//        ByteBuffer buf = getResourceAsByteBuffer("/books.json");
//        Iterator<Object> it = JacksonUtils.readListLazy(buf);
//        assertThat(it.hasNext()).isTrue();
//
//        Object actual1 = it.next();
//        assertThat(actual1).isNotNull();
//        assertThat(actual1).isEqualTo(expected1);
//        assertThat(it.hasNext()).isTrue();
//
//        Object actual2 = it.next();
//        assertThat(actual2).isNotNull();
//        assertThat(actual2).isEqualTo(expected2);
//        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadValueLazyList() throws IOException {
//        Book expected1 = new Book(
//                "Thinking in Java",
//                ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
//                1998,
//                ListUtils.of("Bruce Eckel"));
//        Book expected2 = new Book(
//                "Ready for a victory",
//                ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
//                2020,
//                ListUtils.of("Oleg Cherednik"));
//
//        ByteBuffer buf = getResourceAsByteBuffer("/books.json");
//        Iterator<Book> it = JacksonUtils.readListLazy(buf, Book.class);
//        assertThat(it.hasNext()).isTrue();
//
//        Book actual1 = it.next();
//        assertThat(actual1).isNotNull();
//        assertThat(actual1).isEqualTo(expected1);
//        assertThat(it.hasNext()).isTrue();
//
//        Book actual2 = it.next();
//        assertThat(actual2).isNotNull();
//        assertThat(actual2).isEqualTo(expected2);
//        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveListOfMapWhenRead() throws IOException {
//        ByteBuffer buf = getResourceAsByteBuffer("/data_list.json");
//        List<Map<String, Object>> actual = JacksonUtils.readListOfMap(buf);
//
//        assertThat(actual).hasSize(2);
//        assertThat(actual.get(0)).hasSize(2);
//        assertThat(actual.get(0)).containsEntry("intVal", 555);
//        assertThat(actual.get(0)).containsEntry("strVal", "victory");
//        assertThat(actual.get(1)).hasSize(2);
//        assertThat(actual.get(1)).containsEntry("intVal", 666);
//        assertThat(actual.get(1)).containsEntry("strVal", "omen");
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadByteBufferAsListOfMapLazy() throws IOException {
//        Map<String, Object> expected1 = MapUtils.of(
//                "title", "Thinking in Java",
//                "date", "2017-07-23T13:57:14.225Z",
//                "year", 1998,
//                "authors", ListUtils.of("Bruce Eckel"));
//        Map<String, Object> expected2 = MapUtils.of(
//                "title", "Ready for a victory",
//                "date", "2020-07-23T13:57:14.225Z",
//                "year", 2020,
//                "authors", ListUtils.of("Oleg Cherednik"));
//
//        ByteBuffer buf = getResourceAsByteBuffer("/books.json");
//        Iterator<Map<String, Object>> it = JacksonUtils.readListOfMapLazy(buf);
//        assertThat(it.hasNext()).isTrue();
//
//        Object actual1 = it.next();
//        assertThat(actual1).isNotNull();
//        assertThat(actual1).isEqualTo(expected1);
//        assertThat(it.hasNext()).isTrue();
//
//        Object actual2 = it.next();
//        assertThat(actual2).isNotNull();
//        assertThat(actual2).isEqualTo(expected2);
//        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveDataMapWhenReadAsMapWithStringKey() throws IOException {
//        ByteBuffer buf = getResourceAsByteBuffer("/variable_value_map.json");
//        Map<String, Object> actual = JacksonUtils.readMap(buf);
//        assertThat(actual).isNotNull();
//        assertThat(actual.keySet()).containsExactly("sample", "order");
//        assertThat(actual).containsEntry("sample", ListUtils.of("one, two", "three"));
//        assertThat(actual).containsEntry("order", MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueMapWhenReadAsMapWithStringKeyAndType() throws IOException {
//        ByteBuffer buf = getResourceAsByteBuffer("/string_value_map_s.json");
//        Map<String, String> actual = JacksonUtils.readMap(buf, String.class);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo(MapUtils.of("auto", "Audi", "model", "RS3"));
    }

    public void shouldRetrieveDeserializedMapWhenReadAsMapListWithStringKeyAndBookType() throws IOException {
//        Map<String, Book> expected = MapUtils.of(
//                "one", new Book(
//                        "Thinking in Java",
//                        ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
//                        1998,
//                        ListUtils.of("Bruce Eckel")),
//                "two", new Book(
//                        "Ready for a victory",
//                        ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
//                        2020,
//                        ListUtils.of("Oleg Cherednik"))
//        );
//
//        ByteBuffer buf = getResourceAsByteBuffer("/books_dict_string_key.json");
//        Map<String, Book> actual = JacksonUtils.readMap(buf, Book.class);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveIntegerValueMapWhenReadAsMapWithIntKeyAndBookType() throws IOException {
//        Map<Integer, Book> expected = MapUtils.of(
//                1, new Book(
//                        "Thinking in Java",
//                        ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
//                        1998,
//                        ListUtils.of("Bruce Eckel")),
//                2, new Book(
//                        "Ready for a victory",
//                        ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
//                        2020,
//                        ListUtils.of("Oleg Cherednik"))
//        );
//
//        ByteBuffer buf = getResourceAsByteBuffer("/books_dict_int_key.json");
//        Map<Integer, Book> actual = JacksonUtils.readMap(buf, Integer.class, Book.class);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveEmptyListWhenReadEmptyByteBufferAsList() {
//        assertThat(JacksonUtils.readList(convertToByteBuffer("[]"))).isEmpty();
//        assertThat(JacksonUtils.readList(convertToByteBuffer("[]"), Data.class)).isEmpty();
//        assertThat(JacksonUtils.readSet(convertToByteBuffer("[]"))).isEmpty();
//        assertThat(JacksonUtils.readSet(convertToByteBuffer("[]"), Data.class)).isEmpty();
//        assertThat(JacksonUtils.readListOfMap(convertToByteBuffer("[]"))).isEmpty();
//        assertThat(JacksonUtils.readMap(convertToByteBuffer("{}"))).isEmpty();
//        assertThat(JacksonUtils.readMap(convertToByteBuffer("{}"), Data.class)).isEmpty();
//        assertThat(JacksonUtils.readMap(convertToByteBuffer("{}"), String.class, Data.class)).isEmpty();
    }

    public void shouldThrowJacksonUtilsExceptionWhenReadIncorrectByteBuffer() {
//        ByteBuffer buf = convertToByteBuffer("incorrect");
//
//        assertThatThrownBy(() -> JacksonUtils.readValue(buf, Data.class))
//                .isExactlyInstanceOf(JacksonUtilsException.class);
//        assertThatThrownBy(() -> JacksonUtils.readMap(buf))
//                .isExactlyInstanceOf(JacksonUtilsException.class);
//        assertThatThrownBy(() -> JacksonUtils.readMap(buf, Data.class))
//                .isExactlyInstanceOf(JacksonUtilsException.class);
//        assertThatThrownBy(() -> JacksonUtils.readMap(buf, String.class, Data.class))
//                .isExactlyInstanceOf(JacksonUtilsException.class);
    }

    private static ByteBuffer getResourceAsByteBuffer(String name) throws IOException {
        try (InputStream in = Objects.requireNonNull(ByteBufferJacksonUtilsTest.class.getResourceAsStream(name))) {
            return ByteBuffer.wrap(IOUtils.toByteArray(in));
        }
    }

    private static ByteBuffer convertToByteBuffer(String str) {
        return ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8));
    }

}


