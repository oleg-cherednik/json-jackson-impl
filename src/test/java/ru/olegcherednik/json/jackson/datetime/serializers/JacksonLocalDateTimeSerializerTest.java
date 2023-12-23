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
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class JacksonLocalDateTimeSerializerTest {

    public void shouldUserSuperClassLogicWhenDateFormatIsNull() throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, JacksonLocalDateTimeSerializer.INSTANCE);

        ObjectMapper mapper = new ObjectMapper().registerModule(module);
        String json = mapper.writeValueAsString(LocalDateTime.parse("2017-07-23T13:57:14.225"));
        assertThat(json).isEqualTo("[2017,7,23,13,57,14,225000000]");
    }

}
