/*
 * Copyright © 2016 Oleg Cherednik
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ru.olegcherednik.utils.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
public final class JsonUtils {

    public static <T> T readValue(String json, Class<T> valueType) {
        Objects.requireNonNull(valueType, "'valueType' should not be null");

        if (json == null)
            return null;

        return withRuntimeException(() -> JacsonObjectMapper.mapper().readValue(json, valueType));
    }

    public static <T> List<T> readList(String json, Class<T> valueType) {
        Objects.requireNonNull(valueType, "'valueType' should not be null");

        if (json == null)
            return null;
        if (isEmpty(json))
            return Collections.emptyList();

        return withRuntimeException(() -> {
            ObjectReader reader = JacsonObjectMapper.mapper().readerFor(valueType);
            return reader.<T>readValues(json).readAll();
        });
    }

    public static Map<String, ?> readMap(String json) {
        if (json == null)
            return null;
        if (isEmpty(json))
            return Collections.emptyMap();

        return withRuntimeException(() -> {
            ObjectMapper mapper = JacsonObjectMapper.mapper();
            MapType mapType = mapper.getTypeFactory().constructRawMapType(LinkedHashMap.class);
            return mapper.readValue(json, mapType);
        });
    }

    public static <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null)
            return null;
        if (isEmpty(json))
            return Collections.emptyMap();

        return withRuntimeException(() -> {
            ObjectMapper mapper = JacsonObjectMapper.mapper();
            MapType mapType = mapper.getTypeFactory().constructMapType(LinkedHashMap.class, keyClass, valueClass);
            return mapper.readValue(json, mapType);
        });
    }

    public static <T> String writeValue(T obj) {
        if (obj == null)
            return null;

        return withRuntimeException(() -> JacsonObjectMapper.mapper().writeValueAsString(obj));
    }

    public static <T> void writeValue(T obj, OutputStream out) {
        Objects.requireNonNull(out, "'out' should not be null");

        withRuntimeException(() -> {
            JacsonObjectMapper.mapper().writeValue(out, obj);
            return null;
        });
    }

    public static <T> String writePrettyValue(T obj) throws JsonProcessingException {
        if (obj == null)
            return null;

        return withRuntimeException(() -> JacsonObjectMapper.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj));
    }

    public static <T> void writePrettyValue(T obj, OutputStream out) throws IOException {
        Objects.requireNonNull(out, "'out' should not be null");

        withRuntimeException(() -> {
            JacsonObjectMapper.mapper().writerWithDefaultPrettyPrinter().writeValue(out, obj);
            return null;
        });
    }

    private static <T> T withRuntimeException(Callable<T> task) {
        try {
            return task.call();
        } catch(Exception e) {
            throw new JacksonUtilsException(e);
        }
    }

    private static boolean isEmpty(String json) {
        json = json.trim();
        return "{}".equals(json) || "[]".equals(json);
    }

    private JsonUtils() {
    }
}
