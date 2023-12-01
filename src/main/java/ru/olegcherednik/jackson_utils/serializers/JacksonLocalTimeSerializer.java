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

package ru.olegcherednik.jackson_utils.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 01.12.2023
 */
public class JacksonLocalTimeSerializer extends LocalTimeSerializer {

    private static final long serialVersionUID = 8966284219834243016L;

    public JacksonLocalTimeSerializer(DateTimeFormatter df) {
        super(JacksonInstantSerializer.withZone(df));
    }

    public JacksonLocalTimeSerializer(JacksonLocalTimeSerializer base,
                                      Boolean useTimestamp,
                                      Boolean useNanoseconds,
                                      DateTimeFormatter df) {
        super(base, useTimestamp, useNanoseconds, JacksonInstantSerializer.withZone(df));
    }

    @Override
    protected JacksonLocalTimeSerializer withFormat(Boolean useTimestamp,
                                                    DateTimeFormatter df,
                                                    JsonFormat.Shape shape) {
        return new JacksonLocalTimeSerializer(this, useTimestamp, _useNanoseconds, df);
    }

}
