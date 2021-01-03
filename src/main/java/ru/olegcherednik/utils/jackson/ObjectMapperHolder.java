package ru.olegcherednik.utils.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
public final class ObjectMapperHolder {

    public static final Supplier<ObjectMapper> DEFAULT_MAPPER_BUILDER = new JacksonObjectMapperBuilder();

    private static Supplier<ObjectMapper> mapperBuilder = DEFAULT_MAPPER_BUILDER;
    private static ObjectMapper mapper = createMapper();
    private static ObjectMapper prettyPrintMapper = createPrettyPrintMapper();

    static synchronized ObjectMapper mapper() {
        return mapper;
    }

    static synchronized ObjectMapper prettyPrintMapper() {
        return prettyPrintMapper;
    }

    public static ObjectMapper createMapper() {
        return mapperBuilder.get();
    }

    public static ObjectMapper createPrettyPrintMapper() {
        return mapperBuilder.get().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static ObjectMapper createMapper(Supplier<ObjectMapper> mapperBuilder) {
        return mapperBuilder.get();
    }

    public static ObjectMapper createPrettyPrintMapper(Supplier<ObjectMapper> mapperBuilder) {
        return mapperBuilder.get().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static ObjectMapperDecorator createMapperDecorator(Supplier<ObjectMapper> mapperBuilder) {
        return new ObjectMapperDecorator(createMapper(mapperBuilder));
    }

    public static ObjectMapperDecorator createPrettyPrintMapperDecorator(Supplier<ObjectMapper> mapperBuilder) {
        return new ObjectMapperDecorator(createPrettyPrintMapper(mapperBuilder));
    }

    public static synchronized void setMapperBuilder(Supplier<ObjectMapper> mapperBuilder) {
        mapperBuilder = Optional.ofNullable(mapperBuilder).orElse(DEFAULT_MAPPER_BUILDER);

        if (mapperBuilder != ObjectMapperHolder.mapperBuilder) {
            ObjectMapperHolder.mapperBuilder = mapperBuilder;
            mapper = createMapper();
            prettyPrintMapper = createPrettyPrintMapper();
        }
    }

    private ObjectMapperHolder() {
    }

}
