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

package ru.olegcherednik.json.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

/**
 * @author Oleg Cherednik
 * @since 18.02.2024
 */
public class JacksonSimpleModule extends SimpleModule {

    private static final long serialVersionUID = -543871656247679655L;

    protected JacksonSimpleModule(String name) {
        super(name, Version.unknownVersion());
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        addKeySerializers(context);
        addKeyDeserializers(context);

        addSerializers(context);
        addDeserializers(context);
    }

    protected void addKeySerializers(SetupContext context) {
    }

    protected void addKeyDeserializers(SetupContext context) {
    }

    protected void addSerializers(SetupContext context) {
    }

    protected void addDeserializers(SetupContext context) {
    }

    protected static <T> SimpleSerializers createSerializers(JsonSerializer<T> serializer) {
        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(serializer.handledType(), serializer);
        return serializers;
    }

    protected static <T> SimpleDeserializers createDeserializers(JsonDeserializer<T> deserializer) {
        SimpleDeserializers deserializers = new SimpleDeserializers();
        deserializers.addDeserializer((Class<T>) deserializer.handledType(), deserializer);
        return deserializers;
    }

}
