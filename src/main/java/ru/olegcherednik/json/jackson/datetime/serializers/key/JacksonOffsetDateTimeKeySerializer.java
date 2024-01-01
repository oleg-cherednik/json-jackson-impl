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

package ru.olegcherednik.json.jackson.datetime.serializers.key;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.DecimalUtils;
import ru.olegcherednik.json.api.ZoneModifier;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 01.01.2024
 */
public class JacksonOffsetDateTimeKeySerializer extends JacksonKeySerializer<OffsetDateTime> {

    private static final long serialVersionUID = 1613595140810067069L;

    public JacksonOffsetDateTimeKeySerializer(DateTimeFormatter df) {
        super(OffsetDateTime.class, df, ZoneModifier.USE_ORIGINAL,
              dt -> dt.toInstant().toEpochMilli(), OffsetDateTime::toEpochSecond, OffsetDateTime::getNano);
    }

}
