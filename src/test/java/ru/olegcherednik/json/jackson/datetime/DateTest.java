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

package ru.olegcherednik.json.jackson.datetime;

import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.jackson.MapUtils;
import ru.olegcherednik.json.jackson.ResourceData;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 13.03.2022
 */
@Test
@SuppressWarnings("NewClassNamingConvention")
public class DateTest {

    public void shouldRetrieveJsonWhenWriteDate() throws ParseException {
        Map<String, Date> map = createData();
        String actual = Json.writeValue(map);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("{\"date\":\"2017-07-23T13:57:14.225+03:00\"}");
    }

    public void shouldRetrievePrettyPrintJsonWhenWriteDateMapWithPrettyPrint() throws IOException, ParseException {
        Map<String, Date> map = createData();
        String actual = Json.prettyPrint().writeValue(map);
        String expected = ResourceData.getResourceAsString("/datetime/date.json").trim();

        assertThat(actual).isNotEqualTo(expected);
        assertThat(Json.readMap(actual)).isEqualTo(Json.readMap(expected));
    }

    public void shouldRetrieveDeserializedDateMapWhenReadJsonAsMap() throws ParseException {
        String json = "{\"date\":\"2017-07-23T13:57:14.225+03:00\"}";
        Map<String, Date> expected = createData();
        Map<String, Date> actual = Json.readMap(json, String.class, Date.class);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    private static Map<String, Date> createData() throws ParseException {
        String str = "2017-07-23T13:57:14.225+03:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        return MapUtils.of("date", df.parse(str));
    }

}
