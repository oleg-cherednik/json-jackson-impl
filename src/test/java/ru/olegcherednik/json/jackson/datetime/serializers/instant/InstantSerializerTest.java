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

package ru.olegcherednik.json.jackson.datetime.serializers.instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.Json;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.jackson.datetime.serializers.JacksonInstantSerializer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 25.12.2023
 */
@Test
public class InstantSerializerTest {

    public void shouldUserToStringWhenSerializeWithNullFormatter() {
        JsonSettings settings = JsonSettings.builder()
                                            .instantFormatter(null)
                                            .build();

        assertThat(settings.getInstantFormatter()).isNull();

        Data data = new Data(Instant.parse("2017-07-23T13:57:14.225Z"));
        String json = Json.createWriter(settings).writeValue(data);
        assertThat(json).isEqualTo("{\"map\":{\"2017-07-23T13:57:14.225Z\":\"2017-07-23T13:57:14.225Z\"}}");
    }

//    public void shouldUserFormatterWhenSerializeWithNotNullFormatter() {
//        JsonSettings settings = JsonSettings.builder()
//                                            .instantFormatter(DateTimeFormatter.ofPattern("'[one] 'yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
//                                            .build();
//
//        Data data = new Data(Instant.parse("2017-07-23T13:57:14.225Z"));
//        String json = Json.createWriter(settings).writeValue(data);
//        System.out.println(json);
////        assertThat(json).isEqualTo("{\"map\":{\"2017-07-23T13:57:14.225Z\":\"2017-07-23T13:57:14.225Z\"}}");
//    }

    public void shouldUserSuperClassLogicWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, JacksonInstantSerializer.INSTANCE);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);
        String json = mapper.writeValueAsString(Instant.parse("2023-12-10T19:22:40.758927Z"));
        assertThat(json).isEqualTo("1702236160.758927000");
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    private static final class Data {

        private final Map<Instant, Instant> map;

        @JsonCreator
        private Data(@JsonProperty("map") Map<Instant, Instant> map) {
            this.map = map;
        }

        private Data(Instant instant) {
            this(Collections.singletonMap(instant, instant));
        }

    }

}
