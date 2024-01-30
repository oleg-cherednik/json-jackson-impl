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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 24.12.2023
 */
@Test
public class JacksonInstantDeserializerTest {

    public void shouldApplyLenientWhenDeserializeWithContextFormatter() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instant.class, new JacksonInstantDeserializer(DateTimeFormatter.ISO_INSTANT));

        String json = "{\"one\":\"2023-12-24T07:55:56.314124100Z\"}";
        Data actual = new ObjectMapper().registerModule(module).readValue(json, Data.class);
        Data expected = new Data(Instant.parse("2023-12-24T07:55:56.314124100Z"));
        assertThat(actual).isEqualTo(expected);
    }

    @Getter
    @EqualsAndHashCode
    private static final class Data {

        private final Instant one;

        @JsonCreator
        private Data(@JsonProperty("one")
                     @JsonFormat(lenient = OptBoolean.TRUE)
                     Instant one) {
            this.one = one;
        }

    }

}
