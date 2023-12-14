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

import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonException;
import ru.olegcherednik.json.jackson.utils.data.Book;
import ru.olegcherednik.json.jackson.utils.data.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
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
public class ReaderTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(Json.readValue((Reader) null, Object.class)).isNull();
        assertThat(Json.readListLazy((Reader) null)).isNull();
        assertThat(Json.readListLazy((Reader) null, Object.class)).isNull();
        assertThat(Json.readListOfMapLazy((Reader) null)).isNull();
    }

    public void shouldRetrieveEmptyCollectionWhenObjectNull() {
        assertThat(Json.readList((Reader) null)).isEmpty();
        assertThat(Json.readList((Reader) null, Object.class)).isEmpty();
        assertThat(Json.readSet((Reader) null)).isEmpty();
        assertThat(Json.readSet((Reader) null, Object.class)).isEmpty();
        assertThat(Json.readListOfMap((Reader) null)).isEmpty();
        assertThat(Json.readMap((Reader) null)).isEmpty();
        assertThat(Json.readMap((Reader) null, String.class)).isEmpty();
        assertThat(Json.readMap((Reader) null, String.class, String.class)).isEmpty();
    }

    public void shouldRetrieveDeserializedObjectWhenReadValue() throws IOException {
        Data actual = Json.readValue(getResourceAsReader("/data.json"), Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(Data.OMEN);
    }

    public void shouldRetrieveEmptyDeserializedObjectWhenReadEmptyValue() throws IOException {
        Data actual = Json.readValue(convertToReader("{}"), Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(Data.EMPTY);
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericList() throws IOException {
        List<Object> actual = Json.readList(
                convertToReader("[1,2.0,3.1,12345678912,123456789123456789123456789123456789]"));

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveUniqueValuesWhenReadListNoUniqueValueAsSet() throws IOException {
        Set<Object> actual = Json.readSet(convertToReader("[\"one\",\"two\",\"three\",\"two\",\"one\"]"));

        assertThat(actual).hasSize(3);
        assertThat(actual).containsExactly("one", "two", "three");
    }

    public void shouldRetrieveCorrectNumericWhenObjectContainsDifferentNumericSet() throws IOException {
        Set<Object> actual = Json.readSet(
                convertToReader("[1,2.0,3.1,12345678912,123456789123456789123456789123456789]"));

        assertThat(actual).hasSize(5);
        assertThat(actual).containsExactly(1,
                                           2.0,
                                           3.1,
                                           12345678912L,
                                           new BigInteger("123456789123456789123456789123456789"));
    }

    public void shouldRetrieveDeserializedListWhenReadAsList() throws IOException {
        List<Data> actual = Json.readList(getResourceAsReader("/data_list.json"), Data.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(ListUtils.of(Data.VICTORY, Data.OMEN));
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

        Iterator<Object> it = Json.readListLazy(getResourceAsReader("/books.json"));
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
        Iterator<Book> it = Json.readListLazy(getResourceAsReader("/books.json"), Book.class);
        assertThat(it.hasNext()).isTrue();

        Book actual1 = it.next();
        assertThat(actual1).isNotNull();
        assertThat(actual1).isEqualTo(Book.THINKING_IN_JAVA);
        assertThat(it.hasNext()).isTrue();

        Book actual2 = it.next();
        assertThat(actual2).isNotNull();
        assertThat(actual2).isEqualTo(Book.READY_FOR_A_VICTORY);
        assertThat(it.hasNext()).isFalse();
    }

    public void shouldRetrieveListOfMapWhenRead() throws IOException {
        List<Map<String, Object>> actual = Json.readListOfMap(getResourceAsReader("/data_list.json"));

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

        Iterator<Map<String, Object>> it = Json.readListOfMapLazy(getResourceAsReader("/books.json"));
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
        Map<String, Object> actual = Json.readMap(getResourceAsReader("/variable_value_map.json"));
        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).containsExactly("sample", "order");
        assertThat(actual).containsEntry("sample", ListUtils.of("one, two", "three"));
        assertThat(actual).containsEntry("order", MapUtils.of("key1", "val1", "key2", "val2"));
    }

    public void shouldRetrieveStringValueMapWhenReadAsMapWithStringKeyAndType() throws IOException {
        Map<String, String> actual = Json.readMap(getResourceAsReader("/string_value_map_s.json"), String.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(MapUtils.of("auto", "Audi", "model", "RS3"));
    }

    public void shouldRetrieveDeserializedMapWhenReadAsMapListWithStringKeyAndBookType() throws IOException {
        Map<String, Book> expected = MapUtils.of("one", Book.THINKING_IN_JAVA,
                                                 "two", Book.READY_FOR_A_VICTORY);

        Map<String, Book> actual = Json.readMap(
                getResourceAsReader("/books_dict_string_key.json"), Book.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveIntegerValueMapWhenReadAsMapWithIntKeyAndBookType() throws IOException {
        Map<Integer, Book> expected = MapUtils.of(1, Book.THINKING_IN_JAVA,
                                                  2, Book.READY_FOR_A_VICTORY);

        Map<Integer, Book> actual = Json.readMap(getResourceAsReader("/books_dict_int_key.json"),
                                                 Integer.class, Book.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldRetrieveEmptyListWhenReadEmptyByteBufferAsList() {
        assertThat(Json.readList(convertToReader("[]"))).isEmpty();
        assertThat(Json.readList(convertToReader("[]"), Data.class)).isEmpty();
        assertThat(Json.readSet(convertToReader("[]"))).isEmpty();
        assertThat(Json.readSet(convertToReader("[]"), Data.class)).isEmpty();
        assertThat(Json.readListOfMap(convertToReader("[]"))).isEmpty();
        assertThat(Json.readMap(convertToReader("{}"))).isEmpty();
        assertThat(Json.readMap(convertToReader("{}"), Data.class)).isEmpty();
        assertThat(Json.readMap(convertToReader("{}"), String.class, Data.class)).isEmpty();
    }

    public void shouldThrowJsonExceptionWhenReadIncorrectByteBuffer() {
        assertThatThrownBy(() -> Json.readValue(convertToReader("incorrect"), Data.class))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readMap(convertToReader("incorrect")))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readMap(convertToReader("incorrect"), Data.class))
                .isExactlyInstanceOf(JsonException.class);
        assertThatThrownBy(() -> Json.readMap(convertToReader("incorrect"), String.class, Data.class))
                .isExactlyInstanceOf(JsonException.class);
    }

    @SuppressWarnings("PMD.CloseResource")
    public void shouldCloseInputStreamWhenFinishParse() throws IOException {
        Reader in = getResourceAsReader("/book.json");
        Book actual = Json.readValue(in, Book.class);
        assertThat(actual).isEqualTo(Book.THINKING_IN_JAVA);
        assertThatThrownBy(in::read).isExactlyInstanceOf(IOException.class).hasMessage("Stream closed");
    }

    private static Reader getResourceAsReader(String name) throws IOException {
        InputStream in = Objects.requireNonNull(ReaderTest.class.getResourceAsStream(name));
        return new InputStreamReader(in);
    }

    private static Reader convertToReader(String str) {
        return new StringReader(str);
    }

}


