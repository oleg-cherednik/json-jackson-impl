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

package ru.olegcherednik.jackson_utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.jackson_utils.types.ListMapTypeReference;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 02.01.2021
 */
@RequiredArgsConstructor
public class ObjectMapperDecorator {

    protected final Supplier<ObjectMapper> supplier;

    public ObjectMapperDecorator(ObjectMapper mapper) {
        this(() -> mapper);
    }

    // ---------- read String----------

    public <V> V readValue(String json, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (json == null)
            return null;

        return withRuntimeException(() -> supplier.get().readValue(json, valueClass));
    }

    // @NotNull
    public List<Object> readList(String json) {
        return readList(json, Object.class);
    }

    // @NotNull
    public <V> List<V> readList(String json, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (json == null)
            return Collections.emptyList();

        return withRuntimeException(() -> {
            ObjectReader reader = supplier.get().readerFor(valueClass);

            try (MappingIterator<V> it = reader.readValues(json)) {
                return it.readAll();
            }
        });
    }

    // @NotNull
    public Set<Object> readSet(String json) {
        return readSet(json, Object.class);
    }

    // @NotNull
    public <V> Set<V> readSet(String json, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (json == null)
            return Collections.emptySet();

        return withRuntimeException(() -> {
            ObjectReader reader = supplier.get().readerFor(valueClass);

            try (MappingIterator<V> it = reader.readValues(json)) {
                return it.readAll(new LinkedHashSet<>());
            }
        });
    }

    // @NotNull
    public List<Map<String, Object>> readListOfMap(String json) {
        if (json == null)
            return Collections.emptyList();

        return withRuntimeException(() -> supplier.get().readValue(json, ListMapTypeReference.INSTANCE));
    }

    // @NotNull
    @SuppressWarnings("PMD.LooseCoupling")
    public Map<String, Object> readMap(String json) {
        if (json == null)
            return Collections.emptyMap();

        return withRuntimeException(() -> {
            ObjectMapper mapper = supplier.get();
            MapType mapType = mapper.getTypeFactory().constructRawMapType(LinkedHashMap.class);
            return mapper.readValue(json, mapType);
        });
    }

    // @NotNull
    public <V> Map<String, V> readMap(String json, Class<V> valueClass) {
        return readMap(json, String.class, valueClass);
    }

    // @NotNull
    @SuppressWarnings("PMD.LooseCoupling")
    public <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null)
            return Collections.emptyMap();

        requireNotNullKeyClass(keyClass);
        requireNotNullValueClass(valueClass);

        return withRuntimeException(() -> {
            ObjectMapper mapper = supplier.get();
            MapType mapType = mapper.getTypeFactory().constructMapType(LinkedHashMap.class, keyClass, valueClass);
            return mapper.readValue(json, mapType);
        });
    }

    // ---------- read ByteBuffer----------

    public <V> V readValue(ByteBuffer buf, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (buf == null)
            return null;

        return withInputStream(buf, in -> readValue(in, valueClass));
    }

    // @NotNull
    public List<Object> readList(ByteBuffer buf) {
        if (buf == null)
            return Collections.emptyList();

        return withInputStream(buf, this::readList);
    }

    // @NotNull
    public <V> List<V> readList(ByteBuffer buf, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (buf == null)
            return Collections.emptyList();

        return withInputStream(buf, in -> readList(in, valueClass));
    }

    // @NotNull
    public Set<Object> readSet(ByteBuffer buf) {
        if (buf == null)
            return Collections.emptySet();

        return withInputStream(buf, this::readSet);
    }

    // @NotNull
    public <V> Set<V> readSet(ByteBuffer buf, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (buf == null)
            return Collections.emptySet();

        return withInputStream(buf, in -> readSet(in, valueClass));
    }

    public Iterator<Object> readListLazy(ByteBuffer buf) {
        if (buf == null)
            return null;

        return withInputStream(buf, this::readListLazy);
    }

    public <V> Iterator<V> readListLazy(ByteBuffer buf, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (buf == null)
            return null;

        return withInputStream(buf, in -> readListLazy(in, valueClass));
    }

    public List<Map<String, Object>> readListOfMap(ByteBuffer buf) {
        if (buf == null)
            return Collections.emptyList();

        return withInputStream(buf, this::readListOfMap);
    }

    public Iterator<Map<String, Object>> readListOfMapLazy(ByteBuffer buf) {
        if (buf == null)
            return null;

        return withInputStream(buf, this::readListOfMapLazy);
    }

    // @NotNull
    public Map<String, Object> readMap(ByteBuffer buf) {
        if (buf == null)
            return Collections.emptyMap();

        return withInputStream(buf, this::readMap);
    }

    // @NotNull
    public <V> Map<String, V> readMap(ByteBuffer buf, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (buf == null)
            return Collections.emptyMap();

        return withInputStream(buf, in -> readMap(in, valueClass));
    }

    // @NotNull
    public <K, V> Map<K, V> readMap(ByteBuffer buf, Class<K> keyClass, Class<V> valueClass) {
        requireNotNullKeyClass(keyClass);
        requireNotNullValueClass(valueClass);

        if (buf == null)
            return Collections.emptyMap();

        return withInputStream(buf, in -> readMap(in, keyClass, valueClass));
    }

    // ---------- read InputStream ----------

    public <V> V readValue(InputStream in, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (in == null)
            return null;

        return withRuntimeException(() -> supplier.get().readValue(in, valueClass));
    }

    // @NotNull
    public List<Object> readList(InputStream in) {
        return readList(in, Object.class);
    }

    // @NotNull
    public <V> List<V> readList(InputStream in, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (in == null)
            return Collections.emptyList();

        return withRuntimeException(() -> {
            ObjectReader reader = supplier.get().readerFor(valueClass);
            return reader.<V>readValues(in).readAll();
        });
    }

    // @NotNull
    public Set<Object> readSet(InputStream in) {
        return readSet(in, Object.class);
    }

    // @NotNull
    public <V> Set<V> readSet(InputStream in, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (in == null)
            return Collections.emptySet();

        return withRuntimeException(() -> {
            ObjectReader reader = supplier.get().readerFor(valueClass);
            return reader.<V>readValues(in).readAll(new LinkedHashSet<>());
        });
    }

    // @NotNull
    public List<Map<String, Object>> readListOfMap(InputStream in) {
        if (in == null)
            return Collections.emptyList();

        return withRuntimeException(() -> supplier.get().readValue(in, ListMapTypeReference.INSTANCE));
    }

    public Iterator<Object> readListLazy(InputStream in) {
        return readListLazy(in, Object.class);
    }

    public <V> Iterator<V> readListLazy(InputStream in, Class<V> valueClass) {
        requireNotNullValueClass(valueClass);

        if (in == null)
            return null;

        return withRuntimeException(() -> {
            ObjectReader reader = supplier.get().readerFor(valueClass);
            return reader.readValues(in);
        });
    }

    public Iterator<Map<String, Object>> readListOfMapLazy(InputStream in) {
        if (in == null)
            return null;

        return withRuntimeException(() -> {
            ObjectReader reader = supplier.get().readerFor(Map.class);
            return reader.readValues(in);
        });
    }

    // @NotNull
    @SuppressWarnings("PMD.LooseCoupling")
    public Map<String, Object> readMap(InputStream in) {
        if (in == null)
            return Collections.emptyMap();

        return withRuntimeException(() -> {
            ObjectMapper mapper = supplier.get();
            MapType mapType = mapper.getTypeFactory().constructRawMapType(LinkedHashMap.class);
            return mapper.readValue(in, mapType);
        });
    }

    // @NotNull
    public <V> Map<String, V> readMap(InputStream in, Class<V> valueClass) {
        return readMap(in, String.class, valueClass);
    }

    // @NotNull
    @SuppressWarnings("PMD.LooseCoupling")
    public <K, V> Map<K, V> readMap(InputStream in, Class<K> keyClass, Class<V> valueClass) {
        if (in == null)
            return Collections.emptyMap();

        return withRuntimeException(() -> {
            ObjectMapper mapper = supplier.get();
            MapType mapType = mapper.getTypeFactory().constructMapType(LinkedHashMap.class, keyClass, valueClass);
            return mapper.readValue(in, mapType);
        });
    }

    // ---------- write ----------

    public <V> String writeValue(V obj) {
        if (obj == null)
            return null;

        return withRuntimeException(() -> supplier.get().writeValueAsString(obj));
    }

    public <V> void writeValue(V obj, OutputStream out) {
        requireNotNullOut(out);

        if (obj == null)
            return;

        withRuntimeException(() -> {
            supplier.get().writeValue(out, obj);
            return null;
        });
    }

    public <V> void writeValue(V obj, Writer out) {
        requireNotNullOut(out);

        if (obj == null)
            return;

        withRuntimeException(() -> {
            supplier.get().writeValue(out, obj);
            return null;
        });
    }

    // ---------- convert ----------

    // @NotNull
    public <V> Map<String, Object> convertToMap(V obj) {
        if (obj == null)
            return Collections.emptyMap();

        return withRuntimeException(() -> {
            ObjectMapper mapper = supplier.get();
            return (Map<String, Object>) mapper.convertValue(obj, Map.class);
        });
    }

    // ---------- misc ----------

    private static <V> V withRuntimeException(Callable<V> task) {
        try {
            return task.call();
        } catch (Exception e) {
            throw new JacksonUtilsException(e);
        }
    }

    private static <V> V withInputStream(ByteBuffer buf, Function<InputStream, V> task) {
        try (InputStream in = new ByteBufferInputStream(buf)) {
            return task.apply(in);
        } catch (Exception e) {
            throw new JacksonUtilsException(e);
        }
    }

    private static <T> void requireNotNullOut(T out) {
        Objects.requireNonNull(out, "'out' should not be null");
    }

    private static <K> void requireNotNullKeyClass(Class<K> keyClass) {
        Objects.requireNonNull(keyClass, "'keyClass' should not be null");
    }

    private static <V> void requireNotNullValueClass(Class<V> valueClass) {
        Objects.requireNonNull(valueClass, "'valueClass' should not be null");
    }

}
