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
package ru.olegcherednik.jackson_utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.olegcherednik.jackson_utils.enumid.EnumIdModule;
import ru.olegcherednik.jackson_utils.serializers.JacksonUtilsDateSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonUtilsInstantSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonUtilsLocalDateTimeSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonUtilsLocalTimeSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonUtilsOffsetDateTimeSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonUtilsOffsetTimeSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonUtilsZonedDateTimeSerializer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author Oleg Cherednik
 * @since 02.01.2021
 */
public class JacksonObjectMapperSupplier implements Supplier<ObjectMapper> {

    public static final UnaryOperator<ZoneId> ZONE_MODIFIER_USE_ORIGINAL = zoneId -> zoneId;
    public static final UnaryOperator<ZoneId> ZONE_MODIFIER_TO_UTC = zoneId -> ZoneOffset.UTC;

    protected final UnaryOperator<ZoneId> zoneModifier;
    protected final boolean useMilliseconds;

    public static JacksonObjectMapperSupplier.Builder builder() {
        return new Builder();
    }

    protected JacksonObjectMapperSupplier(Builder builder) {
        zoneModifier = builder.zoneModifier;
        useMilliseconds = builder.useMilliseconds;
    }

    @Override
    public ObjectMapper get() {
        ObjectMapper mapper = new ObjectMapper();
        config(mapper);
        registerModule(mapper);
        return mapper;
    }

    protected ObjectMapper config(ObjectMapper mapper) {
        return mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                     .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

                     .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                     .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

                     .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                     .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                     .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                     .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)

                     .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                     .enable(JsonParser.Feature.ALLOW_COMMENTS)
                     .enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);
    }

    protected ObjectMapper registerModule(ObjectMapper mapper) {
        JacksonUtilsInstantSerializer instantSerializer =
                new JacksonUtilsInstantSerializer(zoneModifier, useMilliseconds);
        JacksonUtilsLocalDateTimeSerializer localDateTimeSerializer =
                new JacksonUtilsLocalDateTimeSerializer(useMilliseconds);
        JacksonUtilsLocalTimeSerializer localTimeSerializer = new JacksonUtilsLocalTimeSerializer(useMilliseconds);
        JacksonUtilsOffsetDateTimeSerializer offsetDateTimeSerializer =
                new JacksonUtilsOffsetDateTimeSerializer(zoneModifier, useMilliseconds);
        JacksonUtilsOffsetTimeSerializer offsetTimeSerializer =
                new JacksonUtilsOffsetTimeSerializer(zoneModifier, useMilliseconds);
        JacksonUtilsZonedDateTimeSerializer dateTimeSerializer =
                new JacksonUtilsZonedDateTimeSerializer(zoneModifier, useMilliseconds);
        JacksonUtilsDateSerializer dateSerializer = new JacksonUtilsDateSerializer(instantSerializer);

        return mapper.registerModule(new ParameterNamesModule())
                     .registerModule(new AfterburnerModule())
                     .registerModule(new EnumIdModule())
                     .registerModule(new JavaTimeModule()
                                             .addSerializer(Instant.class, instantSerializer)
                                             .addSerializer(LocalDateTime.class, localDateTimeSerializer)
                                             .addSerializer(LocalTime.class, localTimeSerializer)
                                             .addSerializer(OffsetDateTime.class, offsetDateTimeSerializer)
                                             .addSerializer(OffsetTime.class, offsetTimeSerializer)
                                             .addSerializer(ZonedDateTime.class, dateTimeSerializer)
                                             .addSerializer(Date.class, dateSerializer)
                                             .addKeySerializer(Instant.class, instantSerializer)
                                             .addKeySerializer(LocalDateTime.class, localDateTimeSerializer)
                                             .addKeySerializer(LocalTime.class, localTimeSerializer)
                                             .addKeySerializer(OffsetDateTime.class, offsetDateTimeSerializer)
                                             .addKeySerializer(OffsetTime.class, offsetTimeSerializer)
                                             .addKeySerializer(ZonedDateTime.class, dateTimeSerializer)
                                             .addKeySerializer(Date.class, dateSerializer));
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        private UnaryOperator<ZoneId> zoneModifier = ZONE_MODIFIER_TO_UTC;
        private boolean useMilliseconds = true;

        public Builder zone(ZoneId zone) {
            return zoneModifier(z -> zone);
        }

        public Builder zoneModifier(UnaryOperator<ZoneId> zoneModifier) {
            this.zoneModifier = Optional.ofNullable(zoneModifier).orElse(ZONE_MODIFIER_TO_UTC);
            return this;
        }

        public Builder withUseMilliseconds(boolean useMilliseconds) {
            this.useMilliseconds = useMilliseconds;
            return this;
        }

        public JacksonObjectMapperSupplier build() {
            return new JacksonObjectMapperSupplier(this);
        }

    }

}
