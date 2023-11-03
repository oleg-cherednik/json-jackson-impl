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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JacksonUtilsHelper {

    public static final Supplier<ObjectMapper> DEFAULT_BUILDER = JacksonObjectMapperSupplier.builder().build();

    private static Supplier<ObjectMapper> mapperBuilder = DEFAULT_BUILDER;
    private static ObjectMapper mapper = createMapper();
    private static ObjectMapper prettyPrintMapper = createPrettyPrintMapper();

    @SuppressWarnings("PMD.DefaultPackage")
    static synchronized ObjectMapper mapper() {
        return mapper;
    }

    @SuppressWarnings("PMD.DefaultPackage")
    static synchronized ObjectMapper prettyPrintMapper() {
        return prettyPrintMapper;
    }

    public static ObjectMapper createMapper() {
        return mapperBuilder.get();
    }

    public static ObjectMapper createPrettyPrintMapper() {
        return mapperBuilder.get().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static ObjectMapper createMapper(Supplier<ObjectMapper> mapperSupplier) {
        return mapperSupplier.get();
    }

    public static ObjectMapper createPrettyPrintMapper(Supplier<ObjectMapper> mapperSupplier) {
        return mapperSupplier.get().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static ObjectMapperDecorator createPrettyPrintMapperDecorator(Supplier<ObjectMapper> mapperSupplier) {
        return new ObjectMapperDecorator(createPrettyPrintMapper(mapperSupplier));
    }

    public static ObjectMapperDecorator createMapperDecorator(Supplier<ObjectMapper> mapperSupplier) {
        return new ObjectMapperDecorator(createMapper(mapperSupplier));
    }

    @SuppressWarnings({ "PMD.AvoidReassigningParameters", "PMD.CompareObjectsWithEquals" })
    public static synchronized void setMapperBuilder(Supplier<ObjectMapper> mapperSupplier) {
        mapperSupplier = Optional.ofNullable(mapperSupplier).orElse(DEFAULT_BUILDER);

        if (mapperSupplier == mapperBuilder)
            return;

        mapperBuilder = mapperSupplier;
        mapper = createMapper();
        prettyPrintMapper = createPrettyPrintMapper();
    }

}
