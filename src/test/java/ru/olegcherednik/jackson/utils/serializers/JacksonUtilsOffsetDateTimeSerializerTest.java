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
package ru.olegcherednik.jackson.utils.serializers;

import org.testng.annotations.Test;
import ru.olegcherednik.jackson.utils.JacksonObjectMapperSupplier;
import ru.olegcherednik.utils.reflection.FieldUtils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

/**
 * @author Oleg Cherednik
 * @since 04.05.2022
 */
@Test
@SuppressWarnings("NewClassNamingConvention")
public class JacksonUtilsOffsetDateTimeSerializerTest {

    public void shouldCreateNewInstanceWhenCallWithManyParameters() {
        Boolean useTimestamp = true;
        Boolean useNanoseconds = false;
        DateTimeFormatter formatter = spy(DateTimeFormatter.BASIC_ISO_DATE);

        JacksonUtilsOffsetDateTimeSerializer base =
                new JacksonUtilsOffsetDateTimeSerializer(JacksonObjectMapperSupplier.ZONE_MODIFIER_USE_ORIGINAL, true);
        JacksonUtilsOffsetDateTimeSerializerTest.LocalJacksonUtilsOffsetDateTimeSerializer actual =
                new JacksonUtilsOffsetDateTimeSerializerTest.LocalJacksonUtilsOffsetDateTimeSerializer(base,
                                                                                                       useTimestamp,
                                                                                                       useNanoseconds,
                                                                                                       formatter);
        assertThat(FieldUtils.<UnaryOperator<ZoneId>>getFieldValue(actual, "zoneModifier"))
                .isSameAs(FieldUtils.<UnaryOperator<ZoneId>>getFieldValue(base, "zoneModifier"));
        assertThat(FieldUtils.<Boolean>getFieldValue(actual, "withMilliseconds"))
                .isEqualTo(FieldUtils.<Boolean>getFieldValue(base, "withMilliseconds"));
        assertThat(FieldUtils.<Boolean>getFieldValue(actual, "_useTimestamp")).isSameAs(useTimestamp);
        assertThat(FieldUtils.<Boolean>getFieldValue(actual, "_useNanoseconds")).isSameAs(useNanoseconds);
        assertThat(FieldUtils.<DateTimeFormatter>getFieldValue(actual, "_formatter")).isSameAs(formatter);
    }

    public void shouldCreateNewInstanceWithFeatures() {
        Boolean writeZoneId = false;
        Boolean writeNanoseconds = true;

        JacksonUtilsOffsetDateTimeSerializer base =
                new JacksonUtilsOffsetDateTimeSerializer(JacksonObjectMapperSupplier.ZONE_MODIFIER_USE_ORIGINAL, true);

        assertThat(FieldUtils.<Boolean>getFieldValue(base, "_useNanoseconds")).isNull();

        JacksonUtilsOffsetDateTimeSerializer actual = base.withFeatures(writeZoneId, writeNanoseconds);
        assertThat(actual).isNotSameAs(base);
        assertThat(FieldUtils.<UnaryOperator<ZoneId>>getFieldValue(actual, "zoneModifier"))
                .isSameAs(FieldUtils.<UnaryOperator<ZoneId>>getFieldValue(base, "zoneModifier"));
        assertThat(FieldUtils.<Boolean>getFieldValue(actual, "withMilliseconds"))
                .isEqualTo(FieldUtils.<Boolean>getFieldValue(base, "withMilliseconds"));
        assertThat(FieldUtils.<Boolean>getFieldValue(actual, "_useTimestamp"))
                .isEqualTo(FieldUtils.<Boolean>getFieldValue(base, "_useTimestamp"));
        assertThat(FieldUtils.<Boolean>getFieldValue(actual, "_useNanoseconds")).isTrue();
        assertThat(FieldUtils.<Boolean>getFieldValue(actual, "_formatter"))
                .isEqualTo(FieldUtils.<Boolean>getFieldValue(base, "_formatter"));
    }

    private static final class LocalJacksonUtilsOffsetDateTimeSerializer extends JacksonUtilsOffsetDateTimeSerializer {

        private static final long serialVersionUID = -1949734232627080273L;

        public LocalJacksonUtilsOffsetDateTimeSerializer(JacksonUtilsOffsetDateTimeSerializer base,
                                                         Boolean useTimestamp,
                                                         Boolean useNanoseconds,
                                                         DateTimeFormatter formatter) {
            super(base, useTimestamp, useNanoseconds, formatter);
        }

    }

}
