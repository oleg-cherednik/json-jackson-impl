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
package ru.olegcherednik.jackson.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import ru.olegcherednik.jackson.utils.serializers.ZoneIdZonedDateTimeSerializer;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Oleg Cherednik
 * @since 02.01.2021
 */
public class JacksonObjectMapperBuilder implements Supplier<ObjectMapper> {

    public static final Function<ZoneId, ZoneId> ZONE_MODIFIER_USE_ORIGINAL = zoneId -> zoneId;
    public static final Function<ZoneId, ZoneId> ZONE_MODIFIER_TO_UTC = zoneId -> ZoneOffset.UTC;

    private final Function<ZoneId, ZoneId> zoneModifier;

    public JacksonObjectMapperBuilder() {
        this(ZONE_MODIFIER_TO_UTC);
    }

    public JacksonObjectMapperBuilder(ZoneId zone) {
        this(z -> zone);
    }

    public JacksonObjectMapperBuilder(Function<ZoneId, ZoneId> zoneModifier) {
        this.zoneModifier = zoneModifier;
    }

    protected void registerModule(ObjectMapper mapper) {
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new AfterburnerModule());
        mapper.registerModule(new JavaTimeModule().addSerializer(ZonedDateTime.class, new ZoneIdZonedDateTimeSerializer(zoneModifier)));
    }

    @Override
    public ObjectMapper get() {
        ObjectMapper mapper = new ObjectMapper();
        registerModule(mapper);

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        mapper.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        mapper.enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);

        return mapper;
    }
}