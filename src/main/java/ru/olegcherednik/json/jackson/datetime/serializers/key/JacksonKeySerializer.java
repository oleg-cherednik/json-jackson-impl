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
import ru.olegcherednik.json.api.JsonSettings;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 25.12.2023
 */
public abstract class JacksonKeySerializer<T extends Temporal> extends StdSerializer<T> {

    private static final long serialVersionUID = 2020854544464342989L;

    protected final DateTimeFormatter df;
    protected final UnaryOperator<ZoneId> zoneModifier;
    protected final ToLongFunction<T> getEpochMillis;
    protected final ToLongFunction<T> getEpochSeconds;
    protected final ToIntFunction<T> getNanoseconds;

    protected JacksonKeySerializer(Class<T> supportedType,
                                   DateTimeFormatter df,
                                   UnaryOperator<ZoneId> zoneModifier,
                                   ToLongFunction<T> getEpochMillis,
                                   ToLongFunction<T> getEpochSeconds,
                                   ToIntFunction<T> getNanoseconds) {
        super(supportedType);
        this.df = df;
        this.zoneModifier = zoneModifier;
        this.getEpochMillis = getEpochMillis;
        this.getEpochSeconds = getEpochSeconds;
        this.getNanoseconds = getNanoseconds;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String fieldName = useTimestamp(provider) ? getTimestampFieldName(value, gen, provider)
                                                  : getStringFieldName(value, gen, provider);

        gen.writeFieldName(fieldName);
    }

    protected String getTimestampFieldName(T value, JsonGenerator gen, SerializerProvider provider) {
        if (useNanoseconds(provider)) {
            long seconds = getEpochSeconds.applyAsLong(value);
            int nanoseconds = getNanoseconds.applyAsInt(value);
            return String.valueOf(DecimalUtils.toBigDecimal(seconds, nanoseconds));
        }

        return String.valueOf(getEpochMillis.applyAsLong(value));
    }

    protected String getStringFieldName(T value, JsonGenerator gen, SerializerProvider provider) {
        if (df == null)
            return value.toString();

        ZoneId zoneId = getZoneId(value, provider);
        zoneId = zoneModifier.apply(zoneId);

        return zoneId == null ? df.format(value) : df.withZone(zoneId).format(value);
    }

    protected ZoneId getZoneId(T value, SerializerProvider provider) {
        ZoneId zoneId = df.getZone();
        zoneId = zoneId == null ? getContextZoneId(provider) : zoneId;
        return zoneId;
    }

    protected ZoneId getContextZoneId(SerializerProvider provider) {
        if (!useContextTimeZone(provider))
            return null;
        if (!provider.getConfig().hasExplicitTimeZone())
            return null;
        return provider.getTimeZone().toZoneId();
    }

    protected boolean useContextTimeZone(SerializerProvider provider) {
        return isEnabled(provider, SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE);
    }

    protected boolean useTimestamp(SerializerProvider provider) {
        return df == null && isEnabled(provider, SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    protected boolean useNanoseconds(SerializerProvider provider) {
        return isEnabled(provider, SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
    }

    protected static boolean isEnabled(SerializerProvider provider, SerializationFeature feature) {
        return provider.isEnabled(feature);
    }

}
