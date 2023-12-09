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
import ru.olegcherednik.json.api.JsonEngineFactory;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.jackson.utils.JacksonEngine;
import ru.olegcherednik.json.jackson.utils.datetime.DateTimeModule;
import ru.olegcherednik.json.jackson.utils.enumid.EnumIdModule;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
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

    private static final StaticJsonEngineFactory INSTANCE = new StaticJsonEngineFactory();

    @SuppressWarnings("unused")
    public static StaticJsonEngineFactory getInstance() {
        return INSTANCE;
    }

    // ---------- JsonEngineFactory ----------

    @Override
    public JacksonEngine createJsonEngine(JsonSettings jsonSettings) {
        ObjectMapper mapper = createMapper(jsonSettings);
        return new JacksonEngine(mapper);
    }

    @Override
    public JacksonEngine createPrettyPrintJsonEngine(JsonSettings settings) {
        ObjectMapper mapper = createMapper(settings).enable(SerializationFeature.INDENT_OUTPUT);
        return new JacksonEngine(mapper);
    }

    // ---------- supplier ----------

    private static ObjectMapper createMapper(JsonSettings settings) {
        Objects.requireNonNull(settings);

        ObjectMapper mapper = new ObjectMapper();
        config(mapper);
        registerModules(mapper, settings);
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

    private static ObjectMapper registerModules(ObjectMapper mapper, JsonSettings settings) {
        return mapper.registerModule(new ParameterNamesModule())
                     .registerModule(new AfterburnerModule())
                     .registerModule(new EnumIdModule())
                     .registerModule(new JavaTimeModule())
                     .registerModule(new DateTimeModule(settings.getInstantFormatter(),
                                                        settings.getLocalDateFormatter(),
                                                        settings.getLocalTimeFormatter(),
                                                        settings.getLocalDateTimeFormatter(),
                                                        settings.getOffsetTimeFormatter(),
                                                        settings.getOffsetDateTimeFormatter(),
                                                        settings.getZoneModifier()));
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
