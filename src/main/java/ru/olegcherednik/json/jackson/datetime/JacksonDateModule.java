/*
 * Copyright Oleg Cherednik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.olegcherednik.json.jackson.datetime;

import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.json.jackson.JacksonSimpleModule;
import ru.olegcherednik.json.jackson.datetime.deserializers.JacksonDateDeserializer;

import java.text.DateFormat;

/**
 * @author Oleg Cherednik
 * @since 09.12.2023
 */
@RequiredArgsConstructor
public class JacksonDateModule extends JacksonSimpleModule {

    private static final long serialVersionUID = -7801651470699380868L;

    protected final DateFormat df;

    @Override
    protected void addKeySerializers(SetupContext context) {
        super.addKeySerializers(context);
        context.addKeySerializers(createSerializers(new DateSerializer(null, df)));
    }

    @Override
    protected void addSerializers(SetupContext context) {
        super.addSerializers(context);
        context.addSerializers(createSerializers(new DateSerializer(null, df)));
    }

    @Override
    protected void addDeserializers(SetupContext context) {
        super.addDeserializers(context);
        context.addDeserializers(createDeserializers(JacksonDateDeserializer.with(df)));
    }

}
