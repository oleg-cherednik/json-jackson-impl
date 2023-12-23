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

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
public class JacksonInstantDeserializer extends InstantDeserializer<Instant> {

    private static final long serialVersionUID = 6304027484218980135L;

    public JacksonInstantDeserializer(DateTimeFormatter df) {
        super(InstantDeserializer.INSTANT, df);
    }

    protected JacksonInstantDeserializer(JacksonInstantDeserializer base, DateTimeFormatter df) {
        super(base, df);
    }

    protected JacksonInstantDeserializer(JacksonInstantDeserializer base, DateTimeFormatter df, Boolean leniency) {
        super(base, df, leniency);
    }

    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    protected JacksonInstantDeserializer withDateFormat(DateTimeFormatter df) {
        return df == _formatter ? this : new JacksonInstantDeserializer(this, df);
    }

    @Override
    protected JacksonInstantDeserializer withLeniency(Boolean leniency) {
        return new JacksonInstantDeserializer(this, _formatter, leniency);
    }

}
