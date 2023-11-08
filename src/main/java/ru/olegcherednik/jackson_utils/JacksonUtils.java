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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JacksonUtils {

    private static final ObjectMapperDecorator DELEGATE = new ObjectMapperDecorator(JacksonUtilsHelper::mapper);
    private static final ObjectMapperDecorator PRETTY_PRINT_DELEGATE =
            new ObjectMapperDecorator(JacksonUtilsHelper::prettyPrintMapper);

    // ---------- read String ----------

    public static <V> V readValue(String json, Class<V> valueClass) {
        return print().readValue(json, valueClass);
    }

    public static List<Object> readList(String json) {
        return print().readList(json);
    }

    public static <V> List<V> readList(String json, Class<V> valueClass) {
        return print().readList(json, valueClass);
    }

    public static Set<Object> readSet(String json) {
        return print().readSet(json);
    }

    public static <V> Set<V> readSet(String json, Class<V> valueClass) {
        return print().readSet(json, valueClass);
    }

    public static List<Map<String, Object>> readListOfMap(String json) {
        return print().readListOfMap(json);
    }

    public static Map<String, Object> readMap(String json) {
        return print().readMap(json);
    }

    public static <V> Map<String, V> readMap(String json, Class<V> valueClass) {
        return print().readMap(json, valueClass);
    }

    public static <K, V> Map<K, V> readMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return print().readMap(json, keyClass, valueClass);
    }

    // ---------- read ByteBuffer ----------

    public static <V> V readValue(ByteBuffer buf, Class<V> valueClass) {
        return print().readValue(buf, valueClass);
    }

    public static List<Object> readList(ByteBuffer buf) {
        return print().readList(buf);
    }

    public static <V> List<V> readList(ByteBuffer buf, Class<V> valueClass) {
        return print().readList(buf, valueClass);
    }

    public static Set<Object> readSet(ByteBuffer buf) {
        return print().readSet(buf);
    }

    public static <V> Set<V> readSet(ByteBuffer buf, Class<V> valueClass) {
        return print().readSet(buf, valueClass);
    }

    public static Iterator<Object> readListLazy(ByteBuffer buf) {
        return print().readListLazy(buf);
    }

    public static <V> Iterator<V> readListLazy(ByteBuffer buf, Class<V> valueClass) {
        return print().readListLazy(buf, valueClass);
    }

    public static List<Map<String, Object>> readListOfMap(ByteBuffer buf) {
        return print().readListOfMap(buf);
    }

    public static Iterator<Map<String, Object>> readListOfMapLazy(ByteBuffer buf) {
        return print().readListOfMapLazy(buf);
    }

    public static Map<String, Object> readMap(ByteBuffer buf) {
        return print().readMap(buf);
    }

    public static <V> Map<String, V> readMap(ByteBuffer buf, Class<V> valueClass) {
        return print().readMap(buf, valueClass);
    }

    public static <K, V> Map<K, V> readMap(ByteBuffer buf, Class<K> keyClass, Class<V> valueClass) {
        return print().readMap(buf, keyClass, valueClass);
    }

    // ---------- read InputStream ----------

    public static <V> V readValue(InputStream in, Class<V> valueClass) {
        return print().readValue(in, valueClass);
    }

    public static List<Object> readList(InputStream in) {
        return print().readList(in);
    }

    public static <V> List<V> readList(InputStream in, Class<V> valueClass) {
        return print().readList(in, valueClass);
    }

    public static Set<Object> readSet(InputStream in) {
        return print().readSet(in);
    }

    public static <V> Set<V> readSet(InputStream in, Class<V> valueClass) {
        return print().readSet(in, valueClass);
    }

    public static List<Map<String, Object>> readListOfMap(InputStream in) {
        return print().readListOfMap(in);
    }

    public static Iterator<Object> readListLazy(InputStream in) {
        return print().readListLazy(in);
    }

    public static <V> Iterator<V> readListLazy(InputStream in, Class<V> valueClass) {
        return print().readListLazy(in, valueClass);
    }

    public static Iterator<Map<String, Object>> readListOfMapLazy(InputStream in) {
        return print().readListOfMapLazy(in);
    }

    public static Map<String, Object> readMap(InputStream in) {
        return print().readMap(in);
    }

    public static <V> Map<String, V> readMap(InputStream in, Class<V> valueClass) {
        return print().readMap(in, valueClass);
    }

    public static <K, V> Map<K, V> readMap(InputStream in, Class<K> keyClass, Class<V> valueClass) {
        return print().readMap(in, keyClass, valueClass);
    }

    // ---------- read InputStream and close ----------

    public static <V> V readValueAndClose(InputStream in, Class<V> valueClass) {
        return readAndClose(in, () -> readValue(in, valueClass));
    }

    public static List<Object> readListAndClose(InputStream in) {
        return readAndClose(in, () -> readList(in));
    }

    public static <V> List<V> readListAndClose(InputStream in, Class<V> valueClass) {
        return readAndClose(in, () -> readList(in, valueClass));
    }

    public static Set<Object> readSetAndClose(InputStream in) {
        return readAndClose(in, () -> readSet(in));
    }

    public static <V> Set<V> readSetAndClose(InputStream in, Class<V> valueClass) {
        return readAndClose(in, () -> readSet(in, valueClass));
    }

    public static List<Map<String, Object>> readListOfMapAndClose(InputStream in) {
        return readAndClose(in, () -> readListOfMap(in));
    }

    public static Map<String, Object> readMapAndClose(InputStream in) {
        return readAndClose(in, () -> readMap(in));
    }

    public static <V> Map<String, V> readMapAndClose(InputStream in, Class<V> valueClass) {
        return readAndClose(in, () -> readMap(in, valueClass));
    }

    public static <K, V> Map<K, V> readMapAndClose(InputStream in, Class<K> keyClass, Class<V> valueClass) {
        return readAndClose(in, () -> readMap(in, keyClass, valueClass));
    }

    @SuppressWarnings({ "LocalVariableNamingConvention", "unused" })
    private static <T> T readAndClose(InputStream in, Supplier<T> task) {
        try (InputStream _in = in) {
            return task.get();
        } catch (IOException e) {
            throw new JacksonUtilsException(e);
        }
    }

    // ---------- write ----------

    public static <V> String writeValue(V obj) {
        return print().writeValue(obj);
    }

    public static <V> void writeValue(V obj, OutputStream out) {
        print().writeValue(obj, out);
    }

    public static <V> void writeValue(V obj, Writer out) {
        print().writeValue(obj, out);
    }

    // ---------- print ----------

    public static ObjectMapperDecorator print() {
        return DELEGATE;
    }

    public static ObjectMapperDecorator prettyPrint() {
        return PRETTY_PRINT_DELEGATE;
    }

    // ---------- convert ----------

    public static <V> Map<String, Object> convertToMap(V obj) {
        return print().convertToMap(obj);
    }

}
