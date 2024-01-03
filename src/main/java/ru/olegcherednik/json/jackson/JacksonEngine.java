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

package ru.olegcherednik.json.jackson;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.json.api.JsonEngine;
import ru.olegcherednik.json.api.iterator.AutoCloseableIterator;
import ru.olegcherednik.json.jackson.types.ListMapTypeReference;
import ru.olegcherednik.json.jackson.types.MappingIteratorDecorator;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
public class JacksonEngine implements JsonEngine {

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
    public <V> AutoCloseableIterator<V> readListLazy(Reader reader, Class<V> valueClass) throws IOException {
        MappingIterator<V> it = mapper.readerFor(valueClass).readValues(reader);
        return new MappingIteratorDecorator<>(it);
    }

    @Override
    public AutoCloseableIterator<Map<String, Object>> readListOfMapLazy(Reader reader) throws IOException {
        MappingIterator<Map<String, Object>> it = mapper.readerFor(Map.class).readValues(reader);
        return new MappingIteratorDecorator<>(it);
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

    // ---------- convert ----------

    @Override
    public <V> Map<String, Object> convertToMap(V obj) {
        MapType mapType = mapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, Object.class);
        return mapper.convertValue(obj, mapType);
    }

}
