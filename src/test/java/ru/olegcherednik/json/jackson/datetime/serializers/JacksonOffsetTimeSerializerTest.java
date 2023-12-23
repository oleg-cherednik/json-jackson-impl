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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.Test;

import java.time.OffsetTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class JacksonOffsetTimeSerializerTest {

    public void shouldUserSuperClassLogicWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(OffsetTime.class, JacksonOffsetTimeSerializer.INSTANCE);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);
        String json = mapper.writeValueAsString(OffsetTime.parse("22:16:19.989648300+03:00"));
        assertThat(json).isEqualTo("[22,16,19,989648300,\"+03:00\"]");
    }

    public void shouldUseFeatureWhenSerializeWithFeature() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(OffsetTime.class, JacksonOffsetTimeSerializer.INSTANCE);

        ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .registerModule(module);

        OffsetTime offsetDateTime = OffsetTime.parse("22:16:19.989648300+03:00");
        String json = mapper.writeValueAsString(new Data(offsetDateTime, offsetDateTime));
        assertThat(json).isEqualTo("{\"one\":[22,16,19,989648300,\"+03:00\"],\"two\":[22,16,19,989,\"+03:00\"]}");
    }

    @Getter
    @RequiredArgsConstructor
    private static class Data {

        @JsonFormat(with = JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        private final OffsetTime one;
        private final OffsetTime two;

    }

}
