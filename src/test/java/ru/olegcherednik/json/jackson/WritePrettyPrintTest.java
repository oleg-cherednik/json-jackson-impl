/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Oleg Cherednik
 * @since 07.01.2021
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class WritePrettyPrintTest {

    public void shouldRetrieveNullWhenObjectNull() {
        assertThat(Json.prettyPrint().writeValue(null)).isNull();
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteObjectWithPrettyPrint() throws IOException {
        String actual = Json.prettyPrint().writeValue(Data.OMEN);
        String expected = ResourceData.getResourceAsString("/data.json").trim();

        assertThat(actual).isNotEqualTo(expected);
        assertThat(Json.readMap(actual)).isEqualTo(Json.readMap(expected));
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteMapObjectWithPrettyPrint() throws IOException {
        Map<String, Data> data = MapUtils.of("victory", Data.VICTORY,
                                             "omen", Data.OMEN);
        String actual = Json.prettyPrint().writeValue(data);
        String expected = ResourceData.getResourceAsString("/data_map.json").trim();

        assertThat(actual).isNotEqualTo(expected);
        assertThat(Json.readMap(actual)).isEqualTo(Json.readMap(expected));
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteListObjectWithPrettyPrint() throws IOException {
        List<Data> data = ListUtils.of(Data.VICTORY, Data.OMEN);
        String actual = Json.prettyPrint().writeValue(data);
        String expected = ResourceData.getResourceAsString("/data_list.json").trim();

        assertThat(actual).isNotEqualTo(expected);
        assertThat(Json.readList(actual)).isEqualTo(Json.readList(expected));
    }

    public void shouldWritePrettyPrintJsonToStreamWhenWriteObjectWithPrettyPrintToWriter() throws IOException {
        try (Writer out = new StringWriter()) {
            Json.prettyPrint().writeValue(Data.OMEN, out);

            String expected = ResourceData.getResourceAsString("/data.json").trim();
            assertThat(out.toString()).isNotEqualTo(expected);
            assertThat(Json.readMap(out.toString())).isEqualTo(Json.readMap(expected));
        }
    }

    public void shouldWritePrettyPrintJsonToStreamWhenWriteObjectWithPrettyPrintToOutputStream() throws IOException {
        try (OutputStream out = new ByteArrayOutputStream()) {
            Json.prettyPrint().writeValue(Data.OMEN, out);

            String expected = ResourceData.getResourceAsString("/data.json").trim();
            assertThat(out.toString()).isNotEqualTo(expected);
            assertThat(Json.readMap(out.toString())).isEqualTo(Json.readMap(expected));
        }
    }

    public void shouldThrownExceptionWhenOutputStreamNull() {
        assertThatThrownBy(() -> Json.writeValue("aaa", (OutputStream) null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("'out' should not be null");
    }

    public void shouldThrownExceptionWhenWriterNull() {
        assertThatThrownBy(() -> Json.writeValue("aaa", (Writer) null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("'writer' should not be null");
    }

}
