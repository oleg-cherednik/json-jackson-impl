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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Oleg Cherednik
 * @since 19.11.2014
 */
final class JacksonObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 6432178737868715292L;

    private static final ThreadLocal<ObjectMapper> THREAD_LOCAL_MAPPER = ThreadLocal.withInitial(JacksonObjectMapper::new);

    public static ObjectMapper mapper() {
        return THREAD_LOCAL_MAPPER.get();
    }

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSSZ";

    private JacksonObjectMapper() {
        registerModule(createJavaTimeModule());
        registerModule(new AfterburnerModule());

        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));

        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }

    private static JavaTimeModule createJavaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
//        module.addSerializer(ZonedDateTime.class,
//                new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS").withZone(ZoneOffset.UTC)));
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());

//        module.addSerializer(Date.class, new LocalDateSerializer());

        return module;
    }

    private static final class LocalDateSerializer extends DateSerializer {

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (_asTimestamp(provider)) {
                gen.writeNumber(_timestamp(value));
            } else if (_customFormat != null) {
                // 21-Feb-2011, tatu: not optimal, but better than alternatives:
                synchronized (_customFormat) {
                    gen.writeString(_customFormat.format(value));
                }
            } else {
                provider.defaultSerializeDateValue(value, gen);
            }
        }
    }
}
