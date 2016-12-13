package cop.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
public final class JsonUtils {
    public static <T> T readValue(String json, Class<T> clazz) throws IOException {
        return json != null ? JacksonObjectMapper.getMapper().readValue(json, clazz) : null;
    }

    public static <T> List<T> readList(String json, Class<T> clazz) throws IOException {
        if (json == null)
            return null;

        ObjectReader reader = JacksonObjectMapper.getMapper().readerFor(clazz);
        MappingIterator<T> it = reader.readValues(json);
        return it.hasNextValue() ? it.readAll() : Collections.emptyList();
    }

    public static <T> Map<String, T> readMap(String json) throws IOException {
        if (json == null)
            return null;

        ObjectReader reader = JacksonObjectMapper.getMapper().readerFor(Map.class);
        MappingIterator<Map<String, T>> it = reader.readValues(json);

        if (it.hasNextValue()) {
            Map<String, T> res = it.next();
            return res.isEmpty() ? Collections.emptyMap() : res;
        }

        return Collections.emptyMap();
    }

    public static <T> String writeValue(T obj) throws JsonProcessingException {
        return obj != null ? JacksonObjectMapper.getMapper().writeValueAsString(obj) : null;
    }

    public static <T> void writeValue(T obj, OutputStream out) throws IOException {
        JacksonObjectMapper.getMapper().writeValue(out, obj);
    }

    private JsonUtils() {
    }
}
