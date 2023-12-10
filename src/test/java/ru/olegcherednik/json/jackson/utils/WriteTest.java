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
import ru.olegcherednik.json.jackson.utils.data.Data;
import ru.olegcherednik.json.api.Json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class WriteTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(Json.writeValue(null)).isNull();
    }

    public void shouldNotWriteToOutputStreamWhenObjectNull() throws IOException {
        try (OutputStream out = mock(OutputStream.class)) {
            Json.writeValue(null, out);
            verifyNoInteractions(out);
        }
    }

    public void shouldNotWriteToWriterWhenObjectNull() throws IOException {
        try (Writer out = mock(Writer.class)) {
            Json.writeValue(null, out);
            verifyNoInteractions(out);
        }
    }

    public void shouldRetrieveJsonWhenWriteObject() {
        Data data = new Data(555, "victory");
        String actual = Json.writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"intVal\":555,\"strVal\":\"victory\"}");
    }

    public void shouldRetrieveJsonWhenWriteMapObject() {
        Map<String, Data> map = MapUtils.of("victory", new Data(555, "victory"),
                                            "omen", new Data(666, "omen"));
        String actual = Json.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"victory\":{\"intVal\":555,\"strVal\":\"victory\"}"
                                             + ",\"omen\":{\"intVal\":666,\"strVal\":\"omen\"}}");
    }

    public void shouldRetrieveJsonWhenWriteListObject() {
        List<Data> data = ListUtils.of(new Data(555, "victory"), new Data(666, "omen"));
        String actual = Json.writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]");
    }

    public void shouldRetrieveEmptyJsonWhenWriteEmptyCollection() {
        assertThat(Json.writeValue(Collections.emptyList())).isEqualTo("[]");
        assertThat(Json.writeValue(Collections.emptyMap())).isEqualTo("{}");
    }

    public void shouldWriteJsonToStreamWhenWriteObjectToWriter() throws IOException {
        try (Writer out = new StringWriter()) {
            Data data = new Data(666, "omen");
            Json.writeValue(data, out);
            assertThat(out).hasToString("{\"intVal\":666,\"strVal\":\"omen\"}");
        }
    }

    public void shouldWriteJsonToStreamWhenWriteObjectToOutputStream() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream()) {
            Data data = new Data(666, "omen");
            Json.writeValue(data, out);
            assertThat(out).hasToString("{\"intVal\":666,\"strVal\":\"omen\"}");
        }
    }

}
