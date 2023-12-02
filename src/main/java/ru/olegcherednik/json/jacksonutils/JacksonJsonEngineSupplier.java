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

package ru.olegcherednik.json.jacksonutils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Getter;
import lombok.Setter;
import ru.olegcherednik.json.api.JsonEngine;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.jacksonutils.enumid.EnumIdModule;
import ru.olegcherednik.json.jacksonutils.serializers.JacksonInstantSerializer;
import ru.olegcherednik.json.jacksonutils.serializers.JacksonLocalDateSerializer;
import ru.olegcherednik.json.jacksonutils.serializers.JacksonLocalDateTimeSerializer;
import ru.olegcherednik.json.jacksonutils.serializers.JacksonLocalTimeSerializer;
import ru.olegcherednik.json.jacksonutils.serializers.JacksonOffsetDateTimeSerializer;
import ru.olegcherednik.json.jacksonutils.serializers.JacksonOffsetTimeSerializer;
import ru.olegcherednik.json.jacksonutils.serializers.JacksonZonedDateTimeSerializer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 20.11.2023
 */
@Setter
@Getter
public class JacksonJsonEngineSupplier implements Supplier<JsonEngine> {

    private JsonSettings jsonSettings = JsonSettings.builder().build();

    @Override
    public JsonEngine get() {
        return new JacksonJsonEngine(createMapper());
    }

    public JsonEngine getPrettyPrint() {
        return new JacksonJsonEngine(createMapper().enable(SerializationFeature.INDENT_OUTPUT));
    }

    protected ObjectMapper createMapper() {
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
                     .disable(JsonParser.Feature.AUTO_CLOSE_SOURCE)
                     .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)

                     .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                     .enable(JsonParser.Feature.ALLOW_COMMENTS)
                     .enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);
    }

    protected ObjectMapper registerModule(ObjectMapper mapper) {
        JacksonInstantSerializer instant = JacksonInstantSerializer.INSTANCE
                .with(jsonSettings.getOffsetTimeFormatter(), jsonSettings.getZoneModifier());
        JacksonLocalTimeSerializer localTime = JacksonLocalTimeSerializer.INSTANCE
                .with(jsonSettings.getLocalTimeFormatter());
        JacksonLocalDateSerializer localDate = JacksonLocalDateSerializer.INSTANCE
                .with(jsonSettings.getLocalDateFormatter());
        JacksonLocalDateTimeSerializer localDateTime = JacksonLocalDateTimeSerializer.INSTANCE
                .with(jsonSettings.getDateTimeFormatter());
        JacksonOffsetTimeSerializer offsetTime = JacksonOffsetTimeSerializer.INSTANCE
                .with(jsonSettings.getOffsetTimeFormatter(), jsonSettings.getZoneModifier());
        JacksonOffsetDateTimeSerializer offsetDateTime = JacksonOffsetDateTimeSerializer.INSTANCE
                .with(jsonSettings.getOffsetTimeFormatter(), jsonSettings.getZoneModifier());
        JacksonZonedDateTimeSerializer zonedDateTime = JacksonZonedDateTimeSerializer.INSTANCE
                .with(jsonSettings.getOffsetTimeFormatter(), jsonSettings.getZoneModifier());

        return mapper.registerModule(new ParameterNamesModule())
                     .registerModule(new AfterburnerModule())
                     .registerModule(new EnumIdModule())
                     .registerModule(new JavaTimeModule()
                                             // serializer
                                             .addSerializer(Instant.class, instant)
                                             .addSerializer(LocalTime.class, localTime)
                                             .addSerializer(LocalDate.class, localDate)
                                             .addSerializer(LocalDateTime.class, localDateTime)
                                             .addSerializer(OffsetTime.class, offsetTime)
                                             .addSerializer(OffsetDateTime.class, offsetDateTime)
                                             .addSerializer(ZonedDateTime.class, zonedDateTime)
//                                             .addSerializer(Date.class, date)
                                             // key serializer
                                             .addKeySerializer(Instant.class, instant)
                                             .addKeySerializer(LocalTime.class, localTime)
                                             .addKeySerializer(LocalDate.class, localDate)
                                             .addKeySerializer(LocalDateTime.class, localDateTime)
                                             .addKeySerializer(OffsetTime.class, offsetTime)
                                             .addKeySerializer(OffsetDateTime.class, offsetDateTime)
                                             .addKeySerializer(ZonedDateTime.class, zonedDateTime)
//                                             .addKeySerializer(Date.class, date)
                     );
    }

}
