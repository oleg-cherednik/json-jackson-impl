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

import java.io.IOException;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class WriteJacksonUtilsTest {

    public void shouldRetrieveNullWhenObjectNull() {
//        assertThat(JacksonUtils.writeValue(null)).isNull();
    }

    public void shouldNotWriteToOutputStreamWhenObjectNull() throws IOException {
//        try (OutputStream out = mock(OutputStream.class)) {
//            JacksonUtils.writeValue(null, out);
//            verifyNoInteractions(out);
//        }
    }

    public void shouldNotWriteToWriterWhenObjectNull() throws IOException {
//        try (Writer out = mock(Writer.class)) {
//            JacksonUtils.writeValue(null, out);
//            verifyNoInteractions(out);
//        }
    }

    public void shouldRetrieveJsonWhenWriteObject() {
//        Data data = new Data(555, "victory");
//        String actual = JacksonUtils.writeValue(data);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo("{\"intVal\":555,\"strVal\":\"victory\"}");
    }

    public void shouldRetrieveJsonWhenWriteMapObject() {
//        Map<String, Data> map = MapUtils.of(
//                "victory", new Data(555, "victory"),
//                "omen", new Data(666, "omen"));
//        String actual = JacksonUtils.writeValue(map);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo("{\"victory\":{\"intVal\":555,\"strVal\":\"victory\"}"
//                                             + ",\"omen\":{\"intVal\":666,\"strVal\":\"omen\"}}");
    }

    public void shouldRetrieveJsonWhenWriteListObject() {
//        List<Data> data = ListUtils.of(new Data(555, "victory"), new Data(666, "omen"));
//        String actual = JacksonUtils.writeValue(data);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo("[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]");
    }

    public void shouldRetrieveEmptyJsonWhenWriteEmptyCollection() {
//        assertThat(JacksonUtils.writeValue(Collections.emptyList())).isEqualTo("[]");
//        assertThat(JacksonUtils.writeValue(Collections.emptyMap())).isEqualTo("{}");
    }

    public void shouldWriteJsonToStreamWhenWriteObjectToWriter() throws IOException {
//        try (Writer out = new StringWriter()) {
//            Data data = new Data(666, "omen");
//            JacksonUtils.writeValue(data, out);
//            assertThat(out).hasToString("{\"intVal\":666,\"strVal\":\"omen\"}");
//        }
    }

    public void shouldWriteJsonToStreamWhenWriteObjectToOutputStream() throws IOException {
//        try (OutputStream out = new ByteArrayOutputStream()) {
//            Data data = new Data(666, "omen");
//            JacksonUtils.writeValue(data, out);
//            assertThat(out).hasToString("{\"intVal\":666,\"strVal\":\"omen\"}");
//        }
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteObjectWithPrettyPrint() {
//        Data data = new Data(555, "victory");
//        String actual = JacksonUtils.prettyPrint().writeValue(data);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo('{' + LINE_SEPARATOR
//                                             + "  \"intVal\" : 555," + LINE_SEPARATOR
//                                             + "  \"strVal\" : \"victory\"" + LINE_SEPARATOR
//                                             + '}');
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteMapObjectWithPrettyPrint() {
//        Map<String, Data> data = MapUtils.of(
//                "victory", new Data(555, "victory"),
//                "omen", new Data(666, "omen"));
//        String actual = JacksonUtils.prettyPrint().writeValue(data);
//        assertThat(actual).isEqualTo('{' + LINE_SEPARATOR
//                                             + "  \"victory\" : {" + LINE_SEPARATOR
//                                             + "    \"intVal\" : 555," + LINE_SEPARATOR
//                                             + "    \"strVal\" : \"victory\"" + LINE_SEPARATOR
//                                             + "  }," + LINE_SEPARATOR
//                                             + "  \"omen\" : {" + LINE_SEPARATOR
//                                             + "    \"intVal\" : 666," + LINE_SEPARATOR
//                                             + "    \"strVal\" : \"omen\"" + LINE_SEPARATOR
//                                             + "  }" + LINE_SEPARATOR
//                                             + '}');
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteListObjectWithPrettyPrint() {
//        List<Data> data = ListUtils.of(new Data(555, "victory"), new Data(666, "omen"));
//        String actual = JacksonUtils.prettyPrint().writeValue(data);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo("[ {" + LINE_SEPARATOR
//                                             + "  \"intVal\" : 555," + LINE_SEPARATOR
//                                             + "  \"strVal\" : \"victory\"" + LINE_SEPARATOR
//                                             + "}, {" + LINE_SEPARATOR
//                                             + "  \"intVal\" : 666," + LINE_SEPARATOR
//                                             + "  \"strVal\" : \"omen\"" + LINE_SEPARATOR
//                                             + "} ]");
    }

    public void shouldWritePrettyPrintJsonToStreamWhenWriteObjectWithPrettyPrintToWriter() throws IOException {
//        try (Writer out = new StringWriter()) {
//            Data data = new Data(666, "omen");
//            JacksonUtils.prettyPrint().writeValue(data, out);
//            assertThat(out).hasToString('{' + LINE_SEPARATOR
//                                                + "  \"intVal\" : 666," + LINE_SEPARATOR
//                                                + "  \"strVal\" : \"omen\"" + LINE_SEPARATOR
//                                                + '}');
//        }
    }

    public void shouldWritePrettyPrintJsonToStreamWhenWriteObjectWithPrettyPrintToOutputStream() throws IOException {
//        try (OutputStream out = new ByteArrayOutputStream()) {
//            Data data = new Data(666, "omen");
//            JacksonUtils.prettyPrint().writeValue(data, out);
//            assertThat(out).hasToString('{' + LINE_SEPARATOR
//                                                + "  \"intVal\" : 666," + LINE_SEPARATOR
//                                                + "  \"strVal\" : \"omen\"" + LINE_SEPARATOR
//                                                + '}');
//        }
    }

    public void shouldThrownExceptionWhenOutputStreamNull() {
//        assertThatThrownBy(() -> JacksonUtils.writeValue("aaa", (OutputStream) null))
//                .isExactlyInstanceOf(NullPointerException.class)
//                .hasMessage("'out' should not be null");
    }

    public void shouldThrownExceptionWhenWriterNull() {
//        assertThatThrownBy(() -> JacksonUtils.writeValue("aaa", (Writer) null))
//                .isExactlyInstanceOf(NullPointerException.class)
//                .hasMessage("'out' should not be null");
    }

}
