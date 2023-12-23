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

package ru.olegcherednik.json.jackson.datetime.deserializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.OffsetTimeDeserializer;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
public class JacksonOffsetTimeDeserializer extends OffsetTimeDeserializer {

    private static final long serialVersionUID = -3619487239995610840L;

    public JacksonOffsetTimeDeserializer(DateTimeFormatter df) {
        super(df);
    }

    protected JacksonOffsetTimeDeserializer(JacksonOffsetTimeDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    protected JacksonOffsetTimeDeserializer(JacksonOffsetTimeDeserializer base,
                                            Boolean leniency,
                                            DateTimeFormatter df,
                                            JsonFormat.Shape shape,
                                            Boolean readTimestampsAsNanosOverride) {
        super(base, leniency, df, shape, readTimestampsAsNanosOverride);
    }

    @Override
    protected JacksonOffsetTimeDeserializer withDateFormat(DateTimeFormatter df) {
        return new JacksonOffsetTimeDeserializer(this, _isLenient, df, _shape, _readTimestampsAsNanosOverride);
    }

    @Override
    protected JacksonOffsetTimeDeserializer withLeniency(Boolean leniency) {
        return new JacksonOffsetTimeDeserializer(this, leniency);
    }

    @Override
    protected JacksonOffsetTimeDeserializer _withFormatOverrides(DeserializationContext ctxt,
                                                                 BeanProperty property,
                                                                 JsonFormat.Value formatOverrides) {
        JacksonOffsetTimeDeserializer deser =
                (JacksonOffsetTimeDeserializer) super._withFormatOverrides(ctxt, property, formatOverrides);
        Boolean readTimestampsAsNanosOverride = formatOverrides.getFeature(
                JsonFormat.Feature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

        return Objects.equals(readTimestampsAsNanosOverride, deser._readTimestampsAsNanosOverride)
               ? deser : new JacksonOffsetTimeDeserializer(deser, deser._isLenient, deser._formatter,
                                                           deser._shape, readTimestampsAsNanosOverride);
    }

}
