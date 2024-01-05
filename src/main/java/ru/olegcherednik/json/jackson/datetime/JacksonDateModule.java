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

package ru.olegcherednik.json.jackson.datetime;

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleKeyDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.json.jackson.datetime.deserializers.JacksonDateDeserializer;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Oleg Cherednik
 * @since 09.12.2023
 */
@RequiredArgsConstructor
public class JacksonDateModule extends SimpleModule {

    private static final long serialVersionUID = -7801651470699380868L;

    protected final DateFormat df;

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        addSerializers(context);
        addKeyDeserializers(context);
        addDeserializers(context);
    }

    private void addSerializers(SetupContext context) {
        SimpleSerializers ser = new SimpleSerializers();
        ser.addSerializer(Date.class, new DateSerializer(null, df));

        context.addSerializers(ser);
        context.addKeySerializers(ser);
    }

    private static void addKeyDeserializers(SetupContext context) {
        SimpleKeyDeserializers des = new SimpleKeyDeserializers();
        context.addKeyDeserializers(des);
    }

    private void addDeserializers(SetupContext context) {
        SimpleDeserializers des = new SimpleDeserializers();
        des.addDeserializer(Date.class, JacksonDateDeserializer.with(df));
        context.addDeserializers(des);
    }

}
