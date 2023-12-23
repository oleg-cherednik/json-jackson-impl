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

import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DateFormat;

/**
 * @author Oleg Cherednik
 * @since 15.12.2023
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JacksonDateDeserializer extends DateDeserializers.DateDeserializer {

    private static final long serialVersionUID = -8000633867087095319L;

    public static final JacksonDateDeserializer INSTANCE = new JacksonDateDeserializer();

    public static JacksonDateDeserializer with(DateFormat df) {
        return new JacksonDateDeserializer(INSTANCE, df, INSTANCE._formatString);
    }

    protected JacksonDateDeserializer(DateDeserializers.DateDeserializer base,
                                      DateFormat df,
                                      String formatString) {
        super(base, df, formatString);
    }

    @Override
    protected JacksonDateDeserializer withDateFormat(DateFormat df, String formatString) {
        return new JacksonDateDeserializer(this, df, formatString);
    }

}
