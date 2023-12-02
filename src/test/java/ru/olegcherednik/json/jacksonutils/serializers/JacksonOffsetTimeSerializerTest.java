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

package ru.olegcherednik.json.jacksonutils.serializers;

import org.testng.annotations.Test;

/**
 * @author Oleg Cherednik
 * @since 04.05.2022
 */
@Test
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class JacksonOffsetTimeSerializerTest {

//    public void shouldCreateNewInstanceBaseOnAnotherInstance() throws IOException {
//        Boolean useTimestamp = false;
//        Boolean useNanoseconds = false;
//        DateTimeFormatter df = DateTimeFormatter.ISO_TIME.withZone(ZoneOffset.UTC);
//
//        JacksonOffsetTimeSerializerTest.LocalJacksonUtilsOffsetTimeSerializer actual =
//                new JacksonOffsetTimeSerializerTest.LocalJacksonUtilsOffsetTimeSerializer(
//                        new JacksonOffsetTimeSerializer(df),
//                        useTimestamp,
//                        useNanoseconds,
//                        df.withZone(LocalZoneId.ASIA_SINGAPORE));
//
//        OffsetTime time = OffsetTime.parse("16:28:45.025811+03:00");
//        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
//
//        actual.serialize(time, jsonGenerator, mock(SerializerProvider.class));
//        verify(jsonGenerator, times(1)).writeString(eq("16:28:45.025811+03:00"));
//    }

//    public void shouldCreateNewInstanceWithFeatures() {
//        Boolean writeZoneId = false;
//        Boolean writeNanoseconds = true;
//
//        JacksonUtilsOffsetTimeSerializer base =
//                new JacksonUtilsOffsetTimeSerializer(JacksonObjectMapperSupplier.ZONE_MODIFIER_USE_ORIGINAL, true);
//
//        assertThat(ReflectionUtils.<Boolean>getFieldValue(base, "_useNanoseconds")).isNull();
//
//        JacksonUtilsOffsetTimeSerializer actual = base.withFeatures(writeZoneId, writeNanoseconds);
//        assertThat(actual).isNotSameAs(base);
//        assertThat(ReflectionUtils.<UnaryOperator<ZoneId>>getFieldValue(actual, "zoneModifier"))
//                .isSameAs(ReflectionUtils.<UnaryOperator<ZoneId>>getFieldValue(base, "zoneModifier"));
//        assertThat(ReflectionUtils.<Boolean>getFieldValue(actual, "useMilliseconds"))
//                .isEqualTo(ReflectionUtils.<Boolean>getFieldValue(base, "useMilliseconds"));
//        assertThat(ReflectionUtils.<Boolean>getFieldValue(actual, "_useTimestamp"))
//                .isEqualTo(ReflectionUtils.<Boolean>getFieldValue(base, "_useTimestamp"));
//        assertThat(ReflectionUtils.<Boolean>getFieldValue(actual, "_useNanoseconds")).isTrue();
//        assertThat(ReflectionUtils.<Boolean>getFieldValue(actual, "_formatter"))
//                .isEqualTo(ReflectionUtils.<Boolean>getFieldValue(base, "_formatter"));
//    }

//    private static final class LocalJacksonUtilsOffsetTimeSerializer extends JacksonOffsetTimeSerializer {
//
//        private static final long serialVersionUID = 178846704191432452L;
//
//        private LocalJacksonUtilsOffsetTimeSerializer(JacksonOffsetTimeSerializer base,
//                                                      Boolean useTimestamp,
//                                                      Boolean useNanoseconds,
//                                                      DateTimeFormatter df) {
//            super(base, useTimestamp, useNanoseconds, df);
//        }
//
//    }

}
