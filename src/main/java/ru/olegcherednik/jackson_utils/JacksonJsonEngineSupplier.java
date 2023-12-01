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
import ru.olegcherednik.jackson_utils.enumid.EnumIdModule;
import ru.olegcherednik.jackson_utils.serializers.JacksonDateSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonInstantSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonLocalDateTimeSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonLocalTimeSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonOffsetDateTimeSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonOffsetTimeSerializer;
import ru.olegcherednik.jackson_utils.serializers.JacksonZonedDateTimeSerializer;
import ru.olegcherednik.json.api.JsonEngine;
import ru.olegcherednik.json.api.JsonSettings;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
        DateTimeFormatter df = jsonSettings.getDateTimeFormatter();

        JacksonInstantSerializer instantSerializer = new JacksonInstantSerializer(df);
        JacksonLocalDateTimeSerializer localDateTimeSerializer = new JacksonLocalDateTimeSerializer(df);
        JacksonLocalTimeSerializer localTimeSerializer = new JacksonLocalTimeSerializer(df);
        JacksonOffsetDateTimeSerializer offsetDateTimeSerializer = new JacksonOffsetDateTimeSerializer(df);
        JacksonOffsetTimeSerializer offsetTimeSerializer = new JacksonOffsetTimeSerializer(df);
        JacksonZonedDateTimeSerializer dateTimeSerializer = new JacksonZonedDateTimeSerializer(df);
        JacksonDateSerializer dateSerializer = new JacksonDateSerializer(instantSerializer);

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

}
