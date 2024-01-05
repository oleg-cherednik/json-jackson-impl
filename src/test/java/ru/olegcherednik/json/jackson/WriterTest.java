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

package ru.olegcherednik.json.jackson;

import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.jackson.data.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class WriterTest {

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
        String actual = Json.writeValue(Data.VICTORY);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"intVal\":555,\"strVal\":\"victory\"}");
    }

    public void shouldRetrieveJsonWhenWriteMapObject() {
        Map<String, Data> map = MapUtils.of("victory", Data.VICTORY,
                                            "omen", Data.OMEN);
        String actual = Json.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"victory\":{\"intVal\":555,\"strVal\":\"victory\"}"
                                             + ",\"omen\":{\"intVal\":666,\"strVal\":\"omen\"}}");
    }

    public void shouldRetrieveJsonWhenWriteListObject() {
        List<Data> data = ListUtils.of(Data.VICTORY, Data.OMEN);
        String actual = Json.writeValue(data);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]");
    }

    public void shouldRetrieveJsonWhenWriteIterator() {
        List<Data> data = new ArrayList<>(ListUtils.of(Data.VICTORY, Data.OMEN));
        String actual = Json.writeValue(data.iterator());
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("[{\"intVal\":555,\"strVal\":\"victory\"},{\"intVal\":666,\"strVal\":\"omen\"}]");
    }

    public void shouldRetrieveEmptyJsonWhenWriteEmptyCollection() {
        assertThat(Json.writeValue(Collections.emptyList())).isEqualTo("[]");
        assertThat(Json.writeValue(Collections.emptyMap())).isEqualTo("{}");
    }

    public void shouldWriteJsonToStreamWhenWriteObjectToWriter() throws IOException {
        try (Writer out = new StringWriter()) {
            Json.writeValue(Data.OMEN, out);
            assertThat(out).hasToString("{\"intVal\":666,\"strVal\":\"omen\"}");
        }
    }

    public void shouldWriteJsonToStreamWhenWriteObjectToOutputStream() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream()) {
            Json.writeValue(Data.OMEN, out);
            assertThat(out).hasToString("{\"intVal\":666,\"strVal\":\"omen\"}");
        }
    }

    @SuppressWarnings("PMD.CloseResource")
    public void shouldCloseWriterWhenFinishParse() throws IOException {
        Writer out = spy(new StringWriter());
        Json.writeValue(Data.OMEN, out);
        verify(out, times(1)).close();
        assertThat(out).hasToString("{\"intVal\":666,\"strVal\":\"omen\"}");
    }

    @SuppressWarnings("PMD.CloseResource")
    public void shouldCloseOutputStreamWhenFinishParse() throws IOException {
        OutputStream out = spy(new ByteArrayOutputStream());
        Json.writeValue(Data.OMEN, out);
        verify(out, times(1)).close();
        assertThat(out).hasToString("{\"intVal\":666,\"strVal\":\"omen\"}");
    }

}
