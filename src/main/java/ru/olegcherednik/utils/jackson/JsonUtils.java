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
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
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
        return withRuntimeException(() -> json == null ? null : JacksonObjectMapper.mapper().readValue(json, valueType));
    }

    public static <T> List<T> readList(String json, Class<T> valueType) {
        Objects.requireNonNull(valueType, "'valueType' should not be null");

        return withRuntimeException(() -> {
            if (json == null)
                return null;

            ObjectReader reader = JacksonObjectMapper.mapper().readerFor(valueType);
            MappingIterator<T> it = reader.readValues(json);
            return it.hasNextValue() ? it.readAll() : Collections.emptyList();
        });
    }

    public static <T> Map<String, T> readMap(String json) {
        return withRuntimeException(() -> {
            if (json == null)
                return null;

            ObjectReader reader = JacksonObjectMapper.mapper().readerFor(Map.class);
            MappingIterator<Map<String, T>> it = reader.readValues(json);

            if (it.hasNextValue()) {
                Map<String, T> res = it.next();
                return res.isEmpty() ? Collections.emptyMap() : res;
            }

            return Collections.emptyMap();
        });
    }

    public static <T> String writeValue(T obj) {
        return withRuntimeException(() -> obj != null ? JacksonObjectMapper.mapper().writeValueAsString(obj) : null);
    }

    public static <T> void writeValue(T obj, OutputStream out) throws IOException {
        JacksonObjectMapper.mapper().writeValue(out, obj);
    }

    public static <T> String writePrettyValue(T obj) throws JsonProcessingException {
        return obj != null ? JacksonObjectMapper.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj) : null;
    }

    public static <T> void writePrettyValue(T obj, OutputStream out) throws IOException {
        JacksonObjectMapper.mapper().writerWithDefaultPrettyPrinter().writeValue(out, obj);
    }

    private static <T> T withRuntimeException(Callable<T> task) {
        try {
            return task.call();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JsonUtils() {
    }
}
