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

package ru.olegcherednik.json.jackson.datetime.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.Test;
import ru.olegcherednik.json.jackson.LocalZoneId;

import java.time.Instant;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class JacksonInstantSerializerTest {

    public void shouldUserSuperClassLogicWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, JacksonInstantSerializer.INSTANCE);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);
        String json = mapper.writeValueAsString(Instant.parse("2023-12-10T19:22:40.758927Z"));
        assertThat(json).isEqualTo("1702236160.758927000");
    }

    public void shouldUseObjectMapperGlobalZoneWhenSerializeWithGlobalZoneSetAndFormatNull()
            throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, new JacksonInstantSerializer(null, zoneId -> LocalZoneId.AUSTRALIA_SYDNEY));

        ObjectMapper mapper = new ObjectMapper()
                .setTimeZone(TimeZone.getTimeZone(LocalZoneId.ASIA_SINGAPORE))
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(module);

        Instant instant = Instant.parse("2023-12-23T19:22:40.758927Z");
        String json = mapper.writeValueAsString(new Data(instant));
        assertThat(json).isEqualTo("{\"one\":\"2023-12-24T03:22:40.758927+08:00\"}");
    }

    public void shouldIgnoreObjectMapperGlobalZoneWhenFeatureDisabled()
            throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, new JacksonInstantSerializer(null, zoneId -> LocalZoneId.AUSTRALIA_SYDNEY));

        ObjectMapper mapper = new ObjectMapper()
                .setTimeZone(TimeZone.getTimeZone(LocalZoneId.ASIA_SINGAPORE))
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .registerModule(module);

        Instant instant = Instant.parse("2023-12-23T19:22:40.758927Z");
        String json = mapper.writeValueAsString(new Data(instant));
        assertThat(json).isEqualTo("{\"one\":\"2023-12-24T06:22:40.758927+11:00\"}");
    }

    public void shouldIgnoreFeatureContextTimeZoneWhenTimeZoneNotSet()
            throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, new JacksonInstantSerializer(null, zoneId -> LocalZoneId.AUSTRALIA_SYDNEY));

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
                .registerModule(module);

        Instant instant = Instant.parse("2023-12-23T19:22:40.758927Z");
        String json = mapper.writeValueAsString(new Data(instant));
        assertThat(json).isEqualTo("{\"one\":\"2023-12-24T06:22:40.758927+11:00\"}");
    }

    @Getter
    @RequiredArgsConstructor
    private static class Data {

        private final Instant one;

    }

}
