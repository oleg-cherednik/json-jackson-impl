package ru.olegcherednik.jackson_utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.jackson_utils.types.ListMapTypeReference;
import ru.olegcherednik.json.JsonEngine;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Oleg Cherednik
 * @since 20.11.2023
 */
@RequiredArgsConstructor
public class JacksonJsonEngine implements JsonEngine {

    private final ObjectMapper mapper;

    // ---------- read String ----------

    @Override
    public <V> V readValue(String json, Class<V> valueClass) throws IOException {
        return mapper.readValue(json, valueClass);
    }

    @Override
    public <V> List<V> readList(String json, Class<V> valueClass) throws IOException {
        ObjectReader reader = mapper.readerFor(valueClass);

        try (MappingIterator<V> it = reader.readValues(json)) {
            return it.readAll();
        }
    }

    @Override
    public <V> Set<V> readSet(String json, Class<V> valueClass) throws IOException {
        ObjectReader reader = mapper.readerFor(valueClass);

        try (MappingIterator<V> it = reader.readValues(json)) {
            return it.readAll(new LinkedHashSet<>());
        }
    }

    @Override
    public List<Map<String, Object>> readListOfMap(String json) throws IOException {
        return mapper.readValue(json, ListMapTypeReference.INSTANCE);
    }

    @Override
    public <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) throws IOException {
        MapType mapType = mapper.getTypeFactory().constructMapType(LinkedHashMap.class, keyClass, valueClass);
        return mapper.readValue(json, mapType);
    }

    // ---------- read Reader ----------

    @Override
    public <V> V readValue(Reader reader, Class<V> valueClass) throws IOException {
        return mapper.readValue(reader, valueClass);
    }

    @Override
    public <V> List<V> readList(Reader reader, Class<V> valueClass) throws IOException {
        return mapper.readerFor(valueClass)
                     .<V>readValues(reader).readAll();
    }

    @Override
    public <V> Set<V> readSet(Reader reader, Class<V> valueClass) throws IOException {
        return mapper.readerFor(valueClass)
                     .<V>readValues(reader)
                     .readAll(new LinkedHashSet<>());
    }

    @Override
    public List<Map<String, Object>> readListOfMap(Reader reader) throws IOException {
        return mapper.readValue(reader, ListMapTypeReference.INSTANCE);
    }

    @Override
    public <V> Iterator<V> readListLazy(Reader reader, Class<V> valueClass) throws IOException {
        return mapper.readerFor(valueClass).readValues(reader);
    }

    @Override
    public Iterator<Map<String, Object>> readListOfMapLazy(Reader reader) throws IOException {
        return mapper.readerFor(Map.class).readValues(reader);
    }

    @Override
    public <K, V> Map<K, V> readMap(Reader reader, Class<K> keyClass, Class<V> valueClass) throws IOException {
        MapType mapType = mapper.getTypeFactory().constructMapType(LinkedHashMap.class, keyClass, valueClass);
        return mapper.readValue(reader, mapType);
    }

    // ---------- write ----------

    @Override
    public <V> String writeValue(V obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    @Override
    public <V> void writeValue(V obj, Writer writer) throws IOException {
        mapper.writeValue(writer, obj);
    }

}
