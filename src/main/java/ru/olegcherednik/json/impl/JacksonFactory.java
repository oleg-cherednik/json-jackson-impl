/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.jackson.JacksonEngine;
import ru.olegcherednik.json.jackson.JacksonNullKeySerializer;
import ru.olegcherednik.json.jackson.datetime.JacksonDateModule;
import ru.olegcherednik.json.jackson.datetime.JacksonJavaTimeModule;
import ru.olegcherednik.json.jackson.enumid.EnumIdModule;

import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.TimeZone;

/**
 * @author Oleg Cherednik
 * @since 06.01.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class JacksonFactory {

    public static JacksonEngine createJsonEngine(JsonSettings settings) {
        ObjectMapper mapper = createMapper(settings);
        return new JacksonEngine(mapper);
    }

    public static JacksonEngine createPrettyPrintJsonEngine(JsonSettings settings) {
        ObjectMapper mapper = createMapper(settings).enable(SerializationFeature.INDENT_OUTPUT);
        return new JacksonEngine(mapper);
    }

    private static ObjectMapper createMapper(JsonSettings settings) {
        Objects.requireNonNull(settings);

        ObjectMapper mapper = new ObjectMapper();
        config(mapper, settings);
        registerModules(mapper, settings);
        return mapper;
    }

    private static ObjectMapper config(ObjectMapper mapper, JsonSettings settings) {
        if (settings.getZoneId() != null)
            mapper.setTimeZone(TimeZone.getTimeZone(settings.getZoneId()));

        mapper.getSerializerProvider().setNullKeySerializer(JacksonNullKeySerializer.INSTANCE);

        JsonInclude.Include contentIncl = settings.isSerializeNullMapValue() ? JsonInclude.Include.ALWAYS
                                                                             : JsonInclude.Include.NON_NULL;
        mapper.configOverride(Map.class)
              .setInclude(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, contentIncl));

        return mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                     .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

                     .setSerializationInclusion(settings.isSerializeNull() ? JsonInclude.Include.ALWAYS
                                                                           : JsonInclude.Include.NON_NULL)

                     .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                     .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                     .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                     .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                     .disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                     .disable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)    // let df choose it

                     .enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
                     .enable(JsonParser.Feature.AUTO_CLOSE_SOURCE)
                     .enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
                     .enable(JsonParser.Feature.ALLOW_COMMENTS)
                     .enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);
    }

    private static ObjectMapper registerModules(ObjectMapper mapper, JsonSettings settings) {
        mapper.registerModule(new JacksonDateModule(settings.getDateFormatter()))
              .registerModule(new EnumIdModule());

        ServiceLoader.load(Module.class).forEach(module -> {
            mapper.registerModule(module);

            if ("jackson-datatype-jsr310".equals(module.getModuleName()))
                mapper.registerModule(JacksonJavaTimeModule.builder()
                                                           .instant(settings.getInstantFormatter())
                                                           .localDate(settings.getLocalDateFormatter())
                                                           .localTime(settings.getLocalTimeFormatter())
                                                           .localDateTime(settings.getLocalDateTimeFormatter())
                                                           .offsetTime(settings.getOffsetTimeFormatter())
                                                           .offsetDateTime(settings.getOffsetDateTimeFormatter())
                                                           .zonedDateTime(settings.getZonedDateTimeFormatter())
                                                           .build());
        });

        return mapper;
    }

}
