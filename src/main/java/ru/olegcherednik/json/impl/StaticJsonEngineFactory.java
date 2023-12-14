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
import ru.olegcherednik.json.api.JsonEngineFactory;
import ru.olegcherednik.json.api.JsonSettings;
import ru.olegcherednik.json.jackson.utils.JacksonEngine;
import ru.olegcherednik.json.jackson.utils.datetime.JacksonDateModule;
import ru.olegcherednik.json.jackson.utils.datetime.JacksonJavaTimeModule;
import ru.olegcherednik.json.jackson.utils.enumid.EnumIdModule;

import java.util.Objects;
import java.util.ServiceLoader;

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

                     .enable(JsonParser.Feature.AUTO_CLOSE_SOURCE)
                     .enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
                     .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                     .enable(JsonParser.Feature.ALLOW_COMMENTS)
                     .enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);
    }

    private static ObjectMapper registerModules(ObjectMapper mapper, JsonSettings settings) {
        mapper.registerModule(new JacksonDateModule(settings.getDateFormatter()))
              .registerModule(new EnumIdModule());

        ServiceLoader.load(Module.class).forEach(module -> {
            if (module instanceof JacksonDateModule
                    || module instanceof JacksonJavaTimeModule
                    || module instanceof EnumIdModule)
                return;

            mapper.registerModule(module);

            if ("jackson-datatype-jsr310".equals(module.getModuleName()))
                mapper.registerModule(new JacksonJavaTimeModule(settings.getInstantFormatter(),
                                                                settings.getLocalDateFormatter(),
                                                                settings.getLocalTimeFormatter(),
                                                                settings.getLocalDateTimeFormatter(),
                                                                settings.getOffsetTimeFormatter(),
                                                                settings.getOffsetDateTimeFormatter(),
                                                                settings.getZoneModifier()));
        });

        return mapper;
    }

}
