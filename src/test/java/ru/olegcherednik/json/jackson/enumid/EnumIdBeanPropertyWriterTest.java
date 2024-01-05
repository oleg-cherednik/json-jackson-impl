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

package ru.olegcherednik.json.jackson.enumid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.Test;
import ru.olegcherednik.json.api.enumid.EnumId;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oleg Cherednik
 * @since 23.12.2023
 */
@Test
public class EnumIdBeanPropertyWriterTest {

    public void shouldUseMethodsWhenSerializeBasedOnMethods() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new EnumIdModule())
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);

        String json = mapper.writeValueAsString(new Data(Auto.MERCEDES));
        assertThat(json).isEqualTo("{\"auto\":\"mercedes\"}");
    }

    @Getter
    @RequiredArgsConstructor
    private static final class Data {

        private final Auto auto;

    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    private enum Auto implements EnumId {

        MERCEDES("mercedes");

        private final String id;

    }
}
