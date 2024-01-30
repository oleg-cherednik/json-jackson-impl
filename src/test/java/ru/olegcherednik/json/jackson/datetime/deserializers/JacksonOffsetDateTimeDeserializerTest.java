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

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 24.12.2023
 */
@Test
@SuppressWarnings("NewClassNamingConvention")
public class JacksonOffsetDateTimeDeserializerTest {

    public void shouldApplyLenientWhenDeserializeWithContextFormatter() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OffsetDateTime.class,
                               new JacksonOffsetDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        String json = "{\"one\":\"2023-12-24T08:12:13.330250600Z\"}";
        Data actual = new ObjectMapper().registerModule(module).readValue(json, Data.class);
        Data expected = new Data(OffsetDateTime.parse("2023-12-24T08:12:13.330250600Z"));
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldDeserializeFromLongWhenLongValueExists() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OffsetDateTime.class,
                               new JacksonOffsetDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        String json = "{\"one\":\"1703405533330\"}";
        Data actual = new ObjectMapper().registerModule(module).readValue(json, Data.class);
        Data expected = new Data(OffsetDateTime.parse("+55948-10-09T22:42:10+03:00"));
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldNotCreateNewInstanceWhenUseSameDataTimeFormat() {
        LocalDeserializer deser = new LocalDeserializer(DateTimeFormatter.ISO_INSTANT);
        JacksonOffsetDateTimeDeserializer deser1 = deser.withDateFormat(DateTimeFormatter.ISO_INSTANT);
        JacksonOffsetDateTimeDeserializer deser2 = deser.withDateFormat(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        assertThat(deser1).isSameAs(deser);
        assertThat(deser2).isNotSameAs(deser);
    }

    @Getter
    @EqualsAndHashCode
    private static final class Data {

        private final OffsetDateTime one;

        @JsonCreator
        private Data(@JsonProperty("one")
                     @JsonFormat(lenient = OptBoolean.TRUE)
                     OffsetDateTime one) {
            this.one = one;
        }

    }

    private static final class LocalDeserializer extends JacksonOffsetDateTimeDeserializer {

        private static final long serialVersionUID = 3316630704361784717L;

        private LocalDeserializer(DateTimeFormatter df) {
            super(df);
        }

    }

}
