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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 24.12.2023
 */
@Test
@SuppressWarnings("NewClassNamingConvention")
public class JacksonZonedDateTimeDeserializerTest {

    public void shouldApplyLenientWhenDeserializeWithContextFormatter() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ZonedDateTime.class,
                               new JacksonZonedDateTimeDeserializer(DateTimeFormatter.ISO_ZONED_DATE_TIME));

        String json = "{\"one\":\"2023-12-24T08:04:37.026931Z\"}";
        Data actual = new ObjectMapper().registerModule(module).readValue(json, Data.class);
        Data expected = new Data(ZonedDateTime.parse("2023-12-24T08:04:37.026931Z"));
        assertThat(actual).isEqualTo(expected);
    }

    public void shouldNotCreateNewInstanceWhenUseSameDataTimeFormat() {
        LocalDeserializer deser = new LocalDeserializer(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        JacksonZonedDateTimeDeserializer deser1 = deser.withDateFormat(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        JacksonZonedDateTimeDeserializer deser2 = deser.withDateFormat(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        assertThat(deser1).isSameAs(deser);
        assertThat(deser2).isNotSameAs(deser);
    }

    @Getter
    @EqualsAndHashCode
    private static final class Data {

        private final ZonedDateTime one;

        @JsonCreator
        private Data(@JsonProperty("one")
                     @JsonFormat(lenient = OptBoolean.TRUE)
                     ZonedDateTime one) {
            this.one = one;
        }

    }

    private static final class LocalDeserializer extends JacksonZonedDateTimeDeserializer {

        private static final long serialVersionUID = 2848401280418377101L;

        private LocalDeserializer(DateTimeFormatter df) {
            super(df);
        }

    }

}
