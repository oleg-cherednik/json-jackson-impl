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

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
public final class JacksonObjectMapper {

    private static Consumer<ObjectMapper> settingsConsumer = DefaultSettingsConsumer.INSTANCE;

    private static final ThreadLocal<ObjectMapper> THREAD_LOCAL_MAPPER = ThreadLocal.withInitial(() -> {
        ObjectMapper mapper = new ObjectMapper();
        settingsConsumer.accept(mapper);
        return mapper;
    });

    public static void setSettingsConsumer(Consumer<ObjectMapper> settingsConsumer) {
        settingsConsumer = Optional.ofNullable(settingsConsumer).orElse(DefaultSettingsConsumer.INSTANCE);

        if (JacksonObjectMapper.settingsConsumer == settingsConsumer)
            return;

        JacksonObjectMapper.settingsConsumer = DefaultSettingsConsumer.INSTANCE;
        THREAD_LOCAL_MAPPER.remove();
    }

    public static ObjectMapper mapper() {
        return THREAD_LOCAL_MAPPER.get();
    }

    public static void remove() {
        THREAD_LOCAL_MAPPER.remove();
    }

    private JacksonObjectMapper() {
    }

}
