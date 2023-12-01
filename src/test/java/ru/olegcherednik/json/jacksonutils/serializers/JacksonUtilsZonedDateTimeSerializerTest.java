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
 * @since 02.01.2021
 */
@Test
@SuppressWarnings("NewClassNamingConvention")
public class JacksonUtilsZonedDateTimeSerializerTest {

    public void shouldCreateNewInstanceWhenCallWithFeature() {
//        LocalZonedDateTimeSerializer actual = new LocalZonedDateTimeSerializer(zoneId -> LocalZoneId.ASIA_SINGAPORE);
//        assertThat(actual.withFeatures(false)).isExactlyInstanceOf(JacksonUtilsZonedDateTimeSerializer.class);
//        assertThat(actual.withFeatures(false, false)).isExactlyInstanceOf(JacksonUtilsZonedDateTimeSerializer.class);
    }

//    private static final class LocalZonedDateTimeSerializer extends JacksonUtilsZonedDateTimeSerializer {
//
//        private static final long serialVersionUID = 1319340992384997514L;
//
//        private LocalZonedDateTimeSerializer(UnaryOperator<ZoneId> withZone) {
//            super(withZone, false);
//        }
//
//        @Override
//        public JacksonUtilsZonedDateTimeSerializer withFeatures(Boolean writeZoneId) {
//            return super.withFeatures(writeZoneId);
//        }
//
//        @Override
//        public JacksonUtilsZonedDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
//            return super.withFeatures(writeZoneId, writeNanoseconds);
//        }
//
//    }

}

