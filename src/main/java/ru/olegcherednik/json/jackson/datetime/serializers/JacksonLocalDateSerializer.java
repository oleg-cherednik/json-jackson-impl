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

package ru.olegcherednik.json.jackson.datetime.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JacksonLocalDateSerializer extends LocalDateSerializer {

    private static final long serialVersionUID = 613205055855252252L;

    public static final JacksonLocalDateSerializer INSTANCE = new JacksonLocalDateSerializer();

    public static JacksonLocalDateSerializer with(DateTimeFormatter df) {
        return new JacksonLocalDateSerializer(INSTANCE, INSTANCE._useTimestamp, df, INSTANCE._shape);
    }

    protected JacksonLocalDateSerializer(JacksonLocalDateSerializer base,
                                         Boolean useTimestamp,
                                         DateTimeFormatter df,
                                         JsonFormat.Shape shape) {
        super(base, useTimestamp, df, shape);
    }

    @Override
    protected JacksonLocalDateSerializer withFormat(Boolean useTimestamp,
                                                    DateTimeFormatter df,
                                                    JsonFormat.Shape shape) {
        return new JacksonLocalDateSerializer(this, useTimestamp, df, shape);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (useTimestamp(provider) || _formatter != null)
            super.serialize(value, gen, provider);
        else
            gen.writeString(value.toString());
    }

}
