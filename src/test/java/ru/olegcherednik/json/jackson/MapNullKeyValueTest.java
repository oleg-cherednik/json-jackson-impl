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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.api.enumid.EnumId;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 06.02.2024
 */
@Test
public class MapNullKeyValueTest {

    private static final String ONE = "one";

    public void shouldSerializeNulWhenWriteMap() {
        Map<String, String> expected = MapUtils.of(ONE, "1",
                                                   "two", null);
        String json = Json.writeValue(expected);
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo("{\"" + ONE + "\":\"1\",\"two\":null}");

        Map<String, String> actual = Json.readMap(json, String.class, String.class);
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldNotSerializeNulWhenWriteMapWithCustomSetting() {
        JsonSettings settings = JsonSettings.builder()
                                            .serializeNullMapValue(false)
                                            .build();

        Map<String, String> expected = MapUtils.of(ONE, "1",
                                                   "two", null);
        String json = Json.createWriter(settings).writeValue(expected);
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo("{\"" + ONE + "\":\"1\"}");

        Map<String, String> actual = Json.readMap(json, String.class, String.class);
        assertThat(actual).containsOnlyKeys(ONE)
                          .containsEntry(ONE, "1");
    }

    public void shouldSerializeNulWhenWriteMapWithEnumKey() {
        Map<Auto, String> expected = MapUtils.of(Auto.AUDI, "RS3",
                                                 Auto.MERCEDES, "G64",
                                                 Auto.BMW, null);
        String json = Json.writeValue(expected);
        assertThat(json).isNotNull();
        assertThat(json).isEqualTo("{\"audi\":\"RS3\",\"mercedes\":\"G64\",\"bmw\":null}");

        Map<Auto, String> actual = Json.readMap(json, Auto.class, String.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public enum Auto implements EnumId {

        AUDI("audi"),
        BMW("bmw"),
        MERCEDES("mercedes");

        private final String id;

        public static Auto parseId(String id) {
            return EnumId.parseId(Auto.class, id);
        }
    }

}
