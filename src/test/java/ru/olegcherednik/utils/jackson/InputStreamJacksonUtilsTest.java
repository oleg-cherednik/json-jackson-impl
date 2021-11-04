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
package ru.olegcherednik.utils.jackson;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 03.01.2021
 */
@Test
public class InputStreamJacksonUtilsTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(JacksonUtils.readValue((InputStream)null, Object.class)).isNull();
        assertThat(JacksonUtils.readList((InputStream)null, Object.class)).isNull();
        assertThat(JacksonUtils.readListLazy(null, Object.class)).isNull();
        assertThat(JacksonUtils.readMap((InputStream)null)).isNull();
        assertThat(JacksonUtils.readMap((InputStream)null, String.class, String.class)).isNull();
    }

    public void shouldRetrieveMapWhenReadInputStreamAsMap() throws IOException {
        try (InputStream in = InputStreamJacksonUtilsTest.class.getResourceAsStream("/book.json")) {
            Map<String, ?> expected = MapUtils.of(
                    "title", "Thinking in Java",
                    "date", "2017-07-23T13:57:14.225Z",
                    "year", 1998,
                    "authors", ListUtils.of("Bruce Eckel")
            );

            Map<String, ?> actual = JacksonUtils.readMap(in);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveDeserializedObjectWhenReadInputStreamAsCustomType() throws IOException {
        try (InputStream in = InputStreamJacksonUtilsTest.class.getResourceAsStream("/book.json")) {
            Book expected = new Book(
                    "Thinking in Java",
                    ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                    1998,
                    ListUtils.of("Bruce Eckel"));
            Book actual = JacksonUtils.readValue(in, Book.class);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveDeserializedListOfObjectsWhenReadInputStreamAsListWithCustomType() throws IOException {
        try (InputStream in = InputStreamJacksonUtilsTest.class.getResourceAsStream("/books.json")) {
            List<Book> expected = ListUtils.of(
                    new Book(
                            "Thinking in Java",
                            ZonedDateTime.parse("2017-07-23T13:57:14.225Z"),
                            1998,
                            ListUtils.of("Bruce Eckel")),
                    new Book(
                            "Ready for a victory",
                            ZonedDateTime.parse("2020-07-23T13:57:14.225Z"),
                            2020,
                            ListUtils.of("Oleg Cherednik"))
            );
            List<Book> actual = JacksonUtils.readList(in, Book.class);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveDeserializedMapWhenReadInputStreamAsMapListWithStringKeyAndCustomType() throws IOException {
        try (InputStream in = InputStreamJacksonUtilsTest.class.getResourceAsStream("/books_dict_string_key.json")) {
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

            Map<String, Book> actual = JacksonUtils.readMap(in, Book.class);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveDeserializedMapWhenReadInputStreamAsMapListWithIntegerKeyAndCustomType() throws IOException {
        try (InputStream in = InputStreamJacksonUtilsTest.class.getResourceAsStream("/books_dict_int_key.json")) {
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

            Map<Integer, Book> actual = JacksonUtils.readMap(in, Integer.class, Book.class);
            assertThat(actual).isNotNull();
            assertThat(actual).isEqualTo(expected);
        }
    }

    public void shouldRetrieveIteratorOfDeserializedObjectsWhenReadInputStreamAsLazyList() throws IOException {
        try (InputStream in = InputStreamJacksonUtilsTest.class.getResourceAsStream("/books.json")) {
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

            Iterator<Book> it = JacksonUtils.readListLazy(in, Book.class);
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
    }

    @SuppressWarnings({ "AssignmentOrReturnOfFieldWithMutableType", "unused" })
    private static final class Book {

        private String title;
        private ZonedDateTime date;
        private int year;
        private List<String> authors;

        public Book() {
        }

        public Book(String title, ZonedDateTime date, int year, List<String> authors) {
            this.title = title;
            this.date = date;
            this.year = year;
            this.authors = authors;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Book))
                return false;

            Book book = (Book)obj;
            return year == book.year && title.equals(book.title) && date.equals(book.date) && authors.equals(book.authors);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, date, year, authors);
        }
    }
}


