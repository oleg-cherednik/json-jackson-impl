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

package ru.olegcherednik.json.jackson.datetime.deserializers;

import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.jackson.MapUtils;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 24.12.2023
 */
@Test
@SuppressWarnings("NewClassNamingConvention")
public class JacksonJsr310KeyDeserializerTest {

    public void shouldSerializeNullKeyAsEmptyString() {
        LocalDate one = LocalDate.parse("2023-05-05");
        LocalDate two = LocalDate.parse("2023-06-06");

        Map<LocalDate, LocalDate> expected = MapUtils.of(one, one,
                                                         null, two);

        String json = Json.writeValue(expected);
        assertThat(json).isEqualTo("{\"2023-05-05\":\"2023-05-05\",\"\":\"2023-06-06\"}");

        Map<LocalDate, LocalDate> actual = Json.readMap(json, LocalDate.class, LocalDate.class);
        assertThat(actual).isEqualTo(expected);
    }

}
