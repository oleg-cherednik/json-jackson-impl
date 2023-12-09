package ru.olegcherednik.json.impl;

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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.olegcherednik.json.api.JsonEngine;
import ru.olegcherednik.json.api.JsonEngineFactory;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.jackson.utils.JacksonJsonEngine;
import ru.olegcherednik.json.jackson.utils.enumid.EnumIdModule;
import ru.olegcherednik.json.jackson.utils.serializers.JacksonDateSerializer;
import ru.olegcherednik.json.jackson.utils.serializers.JacksonInstantSerializer;
import ru.olegcherednik.json.jackson.utils.serializers.JacksonLocalDateSerializer;
import ru.olegcherednik.json.jackson.utils.serializers.JacksonLocalDateTimeSerializer;
import ru.olegcherednik.json.jackson.utils.serializers.JacksonLocalTimeSerializer;
import ru.olegcherednik.json.jackson.utils.serializers.JacksonOffsetDateTimeSerializer;
import ru.olegcherednik.json.jackson.utils.serializers.JacksonOffsetTimeSerializer;
import ru.olegcherednik.json.jackson.utils.serializers.JacksonZonedDateTimeSerializer;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Oleg Cherednik
 * @since 19.11.2023
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaticJsonEngineFactory implements JsonEngineFactory {

    private static final JsonEngineFactory INSTANCE = new StaticJsonEngineFactory();

    @SuppressWarnings("unused")
    public static JsonEngineFactory getInstance() {
        return INSTANCE;
    }

    // ---------- JsonEngineFactory ----------

    @Override
    public JsonEngine createJsonEngine(JsonSettings jsonSettings) {
        ObjectMapper mapper = createMapper(jsonSettings);
        return new JacksonJsonEngine(mapper);
    }

    @Override
    public JsonEngine createPrettyPrintJsonEngine(JsonSettings jsonSettings) {
        ObjectMapper mapper = createMapper(jsonSettings).enable(SerializationFeature.INDENT_OUTPUT);
        return new JacksonJsonEngine(mapper);
    }

    // ---------- supplier ----------

    private static ObjectMapper createMapper(JsonSettings jsonSettings) {
        Objects.requireNonNull(jsonSettings);

        ObjectMapper mapper = new ObjectMapper();
        config(mapper);
        registerModules(mapper);
        registerJsr310Module(mapper, jsonSettings);
//        registerModule(mapper, jsonSettings);
        return mapper;
    }

    private static ObjectMapper config(ObjectMapper mapper) {
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

    private static ObjectMapper registerModules(ObjectMapper mapper) {
        return mapper.registerModule(new ParameterNamesModule())
                     .registerModule(new AfterburnerModule())
                     .registerModule(new EnumIdModule());
    }

    private static ObjectMapper registerJsr310Module(ObjectMapper mapper, JsonSettings jsonSettings) {
        ClassLoader classLoader = StaticJsonEngineFactory.class.getClassLoader();

        try {
            Class<?> cls = classLoader.loadClass("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
            JavaTimeModule javaTimeModule = (JavaTimeModule) cls.newInstance();

            JacksonInstantSerializer instant = JacksonInstantSerializer.INSTANCE
                    .with(jsonSettings.getInstantFormatter(), jsonSettings.getZoneModifier());
            JacksonLocalDateSerializer localDate = JacksonLocalDateSerializer.INSTANCE
                    .with(jsonSettings.getLocalDateFormatter());
            JacksonLocalTimeSerializer localTime = JacksonLocalTimeSerializer.INSTANCE
                    .with(jsonSettings.getLocalTimeFormatter());
            JacksonLocalDateTimeSerializer localDateTime = JacksonLocalDateTimeSerializer.INSTANCE
                    .with(jsonSettings.getLocalDateTimeFormatter());
            JacksonOffsetTimeSerializer offsetTime = JacksonOffsetTimeSerializer.INSTANCE
                    .with(jsonSettings.getOffsetTimeFormatter(), jsonSettings.getZoneModifier());
            JacksonOffsetDateTimeSerializer offsetDateTime = JacksonOffsetDateTimeSerializer.INSTANCE
                    .with(jsonSettings.getOffsetTimeFormatter(), jsonSettings.getZoneModifier());
            JacksonZonedDateTimeSerializer zonedDateTime = JacksonZonedDateTimeSerializer.INSTANCE
                    .with(jsonSettings.getOffsetTimeFormatter(), jsonSettings.getZoneModifier());
            JacksonDateSerializer date = new JacksonDateSerializer(instant);

            mapper.registerModule(javaTimeModule
                                          // serializer
                                          .addSerializer(Instant.class, instant)
                                          .addSerializer(LocalTime.class, localTime)
                                          .addSerializer(LocalDate.class, localDate)
                                          .addSerializer(LocalDateTime.class, localDateTime)
                                          .addSerializer(OffsetTime.class, offsetTime)
                                          .addSerializer(OffsetDateTime.class, offsetDateTime)
                                          .addSerializer(ZonedDateTime.class, zonedDateTime)
                                          .addSerializer(Date.class, date)
                                          // key serializer
                                          .addKeySerializer(Instant.class, instant)
                                          .addKeySerializer(LocalTime.class, localTime)
                                          .addKeySerializer(LocalDate.class, localDate)
                                          .addKeySerializer(LocalDateTime.class, localDateTime)
                                          .addKeySerializer(OffsetTime.class, offsetTime)
                                          .addKeySerializer(OffsetDateTime.class, offsetDateTime)
                                          .addKeySerializer(ZonedDateTime.class, zonedDateTime)
                                          .addKeySerializer(Date.class, date));
        } catch (ClassNotFoundException ignore) {
            // ignore
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return mapper;
    }

    private static ObjectMapper registerModule(ObjectMapper mapper, JsonSettings jsonSettings) {
        JacksonInstantSerializer instant = JacksonInstantSerializer.INSTANCE
                .with(jsonSettings.getInstantFormatter(), jsonSettings.getZoneModifier());
        JacksonLocalTimeSerializer localTime = JacksonLocalTimeSerializer.INSTANCE
                .with(jsonSettings.getLocalTimeFormatter());
        JacksonLocalDateSerializer localDate = JacksonLocalDateSerializer.INSTANCE
                .with(jsonSettings.getLocalDateFormatter());
        JacksonLocalDateTimeSerializer localDateTime = JacksonLocalDateTimeSerializer.INSTANCE
                .with(jsonSettings.getLocalDateTimeFormatter());
        JacksonOffsetTimeSerializer offsetTime = JacksonOffsetTimeSerializer.INSTANCE
                .with(jsonSettings.getOffsetTimeFormatter(), jsonSettings.getZoneModifier());
        JacksonOffsetDateTimeSerializer offsetDateTime = JacksonOffsetDateTimeSerializer.INSTANCE
                .with(jsonSettings.getOffsetDateTimeFormatter(), jsonSettings.getZoneModifier());
        JacksonZonedDateTimeSerializer zonedDateTime = JacksonZonedDateTimeSerializer.INSTANCE
                .with(jsonSettings.getOffsetDateTimeFormatter(), jsonSettings.getZoneModifier());
        JacksonDateSerializer date = new JacksonDateSerializer(instant);

        mapper.registerModule(new JavaTimeModule()
                                      // serializer
                                      .addSerializer(Instant.class, instant)
                                      .addSerializer(LocalTime.class, localTime)
                                      .addSerializer(LocalDate.class, localDate)
                                      .addSerializer(LocalDateTime.class, localDateTime)
                                      .addSerializer(OffsetTime.class, offsetTime)
                                      .addSerializer(OffsetDateTime.class, offsetDateTime)
                                      .addSerializer(ZonedDateTime.class, zonedDateTime)
                                      .addSerializer(Date.class, date)
                                      // key serializer
                                      .addKeySerializer(Instant.class, instant)
                                      .addKeySerializer(LocalTime.class, localTime)
                                      .addKeySerializer(LocalDate.class, localDate)
                                      .addKeySerializer(LocalDateTime.class, localDateTime)
                                      .addKeySerializer(OffsetTime.class, offsetTime)
                                      .addKeySerializer(OffsetDateTime.class, offsetDateTime)
                                      .addKeySerializer(ZonedDateTime.class, zonedDateTime)
                                      .addKeySerializer(Date.class, date));

        return mapper;
    }

    private static Set<String> findJsonEngineFactoryFiles() {
        try {
            Enumeration<URL> urls =
                    getJsonEngineFactoryPaths("com/fasterxml/jackson/datatype/jsr310/JavaTimeModule.class");
            return Collections.list(urls).stream()
                              .map(URL::getFile)
                              .collect(Collectors.toSet());
        } catch (IOException e) {
            return Collections.emptySet();
        }
    }

    private static Enumeration<URL> getJsonEngineFactoryPaths(String name) throws IOException {
        ClassLoader classLoader = StaticJsonEngineFactory.class.getClassLoader();
        return classLoader == null ? ClassLoader.getSystemResources(name) : classLoader.getResources(name);
    }

}
