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

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
public final class JsonUtils {

    private static final ObjectMapperDecorator DELEGATE = new ObjectMapperDecorator(JacksonObjectMapper::mapper);
    private static final ObjectMapperDecorator PRETTY_PRINT_DELEGATE = new ObjectMapperDecorator(JacksonObjectMapper::prettyPrintMapper);

    // ---------- ObjectMapperDecorator ----------

    public static <T> T readValue(String json, Class<T> valueType) {
        return print().readValue(json, valueType);
    }

    public static <T> List<T> readList(String json, Class<T> valueType) {
        return print().readList(json, valueType);
    }

    public static Map<String, ?> readMap(String json) {
        return print().readMap(json);
    }

    public static <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return print().readMap(json, keyClass, valueClass);
    }

    public static <T> String writeValue(T obj) {
        return print().writeValue(obj);
    }

    public static <T> void writeValue(T obj, OutputStream out) {
        print().writeValue(obj, out);
    }

    // ---------- print ----------

    public static ObjectMapperDecorator print() {
        return DELEGATE;
    }

    public static ObjectMapperDecorator prettyPrint() {
        return PRETTY_PRINT_DELEGATE;
    }

    private JsonUtils() {
    }

}
