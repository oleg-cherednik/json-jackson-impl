package cop.utils.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
final class JacksonObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 6353895542108257075L;

    private static final ThreadLocal<ObjectMapper> THREAD_LOCAL_MAPPER = new ThreadLocal<>();
    private static final ThreadLocal<ObjectMapper> THREAD_LOCAL_PRETTY_MAPPER = new ThreadLocal<>();

    public static ObjectMapper getMapper() {
        return getMapper(false);
    }

    public static ObjectMapper getPrettyMapper() {
        return getMapper(true);
    }

    private static ObjectMapper getMapper(boolean prettyPrint) {
        ThreadLocal<ObjectMapper> threadLocal = prettyPrint ? THREAD_LOCAL_PRETTY_MAPPER : THREAD_LOCAL_MAPPER;
        ObjectMapper mapper = threadLocal.get();

        if (mapper == null)
            threadLocal.set(mapper = new JacksonObjectMapper(prettyPrint));

        return mapper;
    }

    private JacksonObjectMapper(boolean prettyPrint) {
        registerModule(new SimpleModule(getClass().getSimpleName(), new Version(1, 0, 0, null, null, null)));
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (prettyPrint)
            enable(SerializationFeature.INDENT_OUTPUT);
    }
}
