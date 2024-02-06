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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 06.02.2024
 */
@Test
public class MapNullKeyValueTest {

    public void shouldSerializeNulWhenWriteMap() {
        Map<String, String> expected = MapUtils.of("one", "1",
                                                   "two", null);
        String json = Json.writeValue(expected);
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo("{\"one\":\"1\",\"two\":null}");

        Map<String, String> actual = Json.readMap(json, String.class, String.class);
        assertThat(actual).isEqualTo(expected);

    }

}
