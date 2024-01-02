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
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JacksonZonedDateTimeSerializer extends ZonedDateTimeSerializer {

    private static final long serialVersionUID = -6441103051278765460L;

    public static final JacksonZonedDateTimeSerializer INSTANCE = new JacksonZonedDateTimeSerializer();

    public static JacksonZonedDateTimeSerializer with(DateTimeFormatter df) {
        return new JacksonZonedDateTimeSerializer(INSTANCE,
                                                  INSTANCE._useTimestamp,
                                                  INSTANCE._useNanoseconds,
                                                  df,
                                                  INSTANCE._shape,
                                                  Optional.ofNullable(INSTANCE._writeZoneId).orElse(false));
    }

    protected JacksonZonedDateTimeSerializer(JacksonZonedDateTimeSerializer base,
                                             Boolean useTimestamp,
                                             Boolean useNanoseconds,
                                             DateTimeFormatter df,
                                             JsonFormat.Shape shape,
                                             Boolean writeZoneId) {
        super(base, useTimestamp, useNanoseconds, df, shape, writeZoneId);
    }

    @Override
    protected JacksonZonedDateTimeSerializer withFormat(Boolean useTimestamp,
                                                        DateTimeFormatter df,
                                                        JsonFormat.Shape shape) {
        return new JacksonZonedDateTimeSerializer(this, useTimestamp, _useNanoseconds, df, shape, _writeZoneId);
    }

    @Override
    protected JacksonZonedDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new JacksonZonedDateTimeSerializer(this, _useTimestamp, writeNanoseconds,
                                                  _formatter, _shape, writeZoneId);
    }

    @Override
    protected String formatValue(ZonedDateTime value, SerializerProvider provider) {
        if (_formatter == null)
            return value.toString();

        DateTimeFormatter df = _formatter;

        if (_formatter.getZone() == null
                && provider.getConfig().hasExplicitTimeZone()
                && provider.isEnabled(WRITE_DATES_WITH_CONTEXT_TIME_ZONE)) {
            ZoneId zoneId = provider.getTimeZone().toZoneId();
            df = df.withZone("UTC".equals(zoneId.getId()) ? ZoneOffset.UTC : zoneId);
        }

        return df.format(value);
    }

}
