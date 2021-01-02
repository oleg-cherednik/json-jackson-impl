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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
public final class JacksonObjectMapper {

    public static final Supplier<ObjectMapper> DEFAULT_MAPPER_BUILDER = new JacksonObjectMapperBuilder();

    private static Supplier<ObjectMapper> mapperBuilder = DEFAULT_MAPPER_BUILDER;
    private static ObjectMapper mapper = createMapper();
    private static ObjectMapper prettyPrintMapper = createPrettyPrintMapper();

    static synchronized ObjectMapper mapper() {
        return mapper;
    }

    static synchronized ObjectMapper prettyPrintMapper() {
        return prettyPrintMapper;
    }

    public static ObjectMapper createMapper() {
        return mapperBuilder.get();
    }

    public static ObjectMapper createPrettyPrintMapper() {
        return mapperBuilder.get().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static ObjectMapper createMapper(Supplier<ObjectMapper> mapperBuilder) {
        return mapperBuilder.get();
    }

    public static ObjectMapper createPrettyPrintMapper(Supplier<ObjectMapper> mapperBuilder) {
        return mapperBuilder.get().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static ObjectMapperDecorator createMapperDecorator(Supplier<ObjectMapper> mapperBuilder) {
        return new ObjectMapperDecorator(createMapper(mapperBuilder));
    }

    public static ObjectMapperDecorator createPrettyPrintMapperDecorator(Supplier<ObjectMapper> mapperBuilder) {
        return new ObjectMapperDecorator(createPrettyPrintMapper(mapperBuilder));
    }

    public static synchronized void setMapperBuilder(Supplier<ObjectMapper> mapperBuilder) {
        JacksonObjectMapper.mapperBuilder = Optional.ofNullable(mapperBuilder).orElse(DEFAULT_MAPPER_BUILDER);
        mapper = createMapper();
        prettyPrintMapper = createPrettyPrintMapper();
    }

    private JacksonObjectMapper() {
    }

}
