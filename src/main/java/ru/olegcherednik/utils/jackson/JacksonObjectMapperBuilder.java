package ru.olegcherednik.utils.jackson;

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

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 02.01.2021
 */
public class JacksonObjectMapperBuilder implements Supplier<ObjectMapper> {

    private final ZoneId zone;

    public JacksonObjectMapperBuilder() {
        this(ZoneOffset.UTC);
    }

    public JacksonObjectMapperBuilder(ZoneId zone) {
        this.zone = zone;
    }

    protected void registerModule(ObjectMapper mapper) {
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new AfterburnerModule());
        mapper.registerModule(new JavaTimeModule().addSerializer(ZonedDateTime.class, new ZoneIdZonedDateTimeSerializer(zone)));
    }

    @Override
    public ObjectMapper get() {
        ObjectMapper mapper = new ObjectMapper();
        registerModule(mapper);

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        mapper.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        mapper.enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);
        return mapper;
    }
}
