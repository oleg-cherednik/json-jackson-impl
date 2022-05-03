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
package ru.olegcherednik.jackson.utils.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Oleg Cherednik
 * @since 02.05.2022
 */
public class JacksonUtilsLocalTimeSerializer extends LocalTimeSerializer {

    private static final long serialVersionUID = -499291458929437608L;

    private final boolean withMilliseconds;

    public JacksonUtilsLocalTimeSerializer(boolean withMilliseconds) {
        this(null, withMilliseconds);
    }

    public JacksonUtilsLocalTimeSerializer(DateTimeFormatter df, boolean withMilliseconds) {
        super(df);
        this.withMilliseconds = withMilliseconds;
    }

    @Override
    protected JacksonUtilsLocalTimeSerializer withFormat(Boolean useTimestamp,
                                                         DateTimeFormatter df,
                                                         JsonFormat.Shape shape) {
        return new JacksonUtilsLocalTimeSerializer(df, withMilliseconds);
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (_formatter == null) {
            value = withMilliseconds ? value : value.truncatedTo(ChronoUnit.SECONDS);
        }

        super.serialize(value, gen, provider);
    }

}
